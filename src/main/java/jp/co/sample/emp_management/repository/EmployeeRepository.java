package jp.co.sample.emp_management.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.sample.emp_management.domain.Employee;

/**
 * employeesテーブルを操作するリポジトリ.
 * 
 * @author igamasayuki
 * 
 */
@Repository
public class EmployeeRepository {

	/**
	 * Employeeオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (rs, i) -> {
		Employee employee = new Employee();
		employee.setId(rs.getInt("id"));
		employee.setName(rs.getString("name"));
		employee.setImage(rs.getString("image"));
		employee.setGender(rs.getString("gender"));
		employee.setHireDate(rs.getDate("hire_date"));
		employee.setMailAddress(rs.getString("mail_address"));
		employee.setZipCode(rs.getString("zip_code"));
		employee.setAddress(rs.getString("address"));
		employee.setTelephone(rs.getString("telephone"));
		employee.setSalary(rs.getInt("salary"));
		employee.setCharacteristics(rs.getString("characteristics"));
		employee.setDependentsCount(rs.getInt("dependents_count"));
		return employee;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * 従業員一覧情報を入社日順、次いでID順(いずれも昇順)で取得します.
	 * 
	 * @return 全従業員一覧 従業員が存在しない場合はサイズ0件の従業員一覧を返します
	 */
	public List<Employee> findAll() {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees ORDER BY hire_date, id";

		List<Employee> developmentList = template.query(sql, EMPLOYEE_ROW_MAPPER);

		return developmentList;
	}

	/**
	 * 主キーから従業員情報を取得します.
	 * 
	 * @param id 検索したい従業員ID
	 * @return 検索された従業員情報
	 * @exception 従業員が存在しない場合は例外を発生します
	 */
	public Employee load(Integer id) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE id=:id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Employee development = template.queryForObject(sql, param, EMPLOYEE_ROW_MAPPER);

		return development;
	}

	/**
	 * 名前の一部から従業員情報を部分一致検索します.
	 * 
	 * @param partOfName 検索したい文字列
	 * @return 検索された従業員のリスト
	 */
	public List<Employee> findByPartOfName(String partOfName) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE name LIKE :name ORDER BY hire_date, id;";

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + partOfName + "%");

		return template.query(sql, param, EMPLOYEE_ROW_MAPPER);
	}

	/**
	 * 名前の一部から従業員の名前を部分一致検索します.
	 * 
	 * @param partOfName 検索したい文字列
	 * @return 検索された従業員名のリスト
	 */
	public List<String> findNameByPartOfName(String partOfName) {
		String sql = "SELECT name FROM employees WHERE name LIKE :name ORDER BY hire_date, id;";

		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + partOfName + "%");

		return template.queryForList(sql, param, String.class);
	}

	/**
	 * 従業員を追加します.
	 * 
	 * @param employee 追加する従業員情報
	 */
	public void insert(Employee employee) {
		String sql = "INSERT INTO employees VALUES ((SELECT max(id) FROM employees)+1,:name,:image,:gender,:hireDate,:mailAddress,:zipCode,:address,:telephone,:salary,:characteristics,:dependentsCount);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
		template.update(sql, param);
	}

	/**
	 * 従業員情報を変更します.
	 */
	public void update(Employee employee) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);

		String updateSql = "UPDATE employees SET dependents_count=:dependentsCount WHERE id=:id";
		template.update(updateSql, param);
	}

	/**
	 * 画像のe〇.pngの〇の中で最大のものを取得.
	 * 
	 * @return 取得された番号
	 */
	public Integer findMaxPictureId() {
		String sql = "SELECT MAX(TRANSLATE(TRANSLATE(image,'e',''),'.png','')) from employees;";
		return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
	}
}
