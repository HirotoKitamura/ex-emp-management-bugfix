package jp.co.sample.emp_management.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * 従業員情報を全件取得します.
	 * 
	 * @return 従業員情報一覧
	 */
	public List<Employee> showList() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}

	/**
	 * 従業員情報を取得します.
	 * 
	 * @param id ID
	 * @return 従業員情報
	 * @throws 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}

	/**
	 * 名前の一部から従業員情報を部分一致検索します.
	 * 
	 * 空欄(スペース含む)で検索した場合は全件検索をします
	 * 
	 * @param partOfName 検索したい文字列
	 * @return 検索された従業員のリスト
	 */
	public List<Employee> searchByPartOfName(String partOfName) {
		if (partOfName == null || "".equals(partOfName.trim().replace("　", ""))) {
			return employeeRepository.findAll();
		}
		return employeeRepository.findByPartOfName(partOfName);
	}

	/**
	 * 名前の一部から従業員名を部分一致検索します.
	 * 
	 * @param partOfName 検索したい文字列
	 * @return 検索された従業員名のリスト
	 */
	public List<String> searchNameByPartOfName(String partOfName) {

		return employeeRepository.findNameByPartOfName(partOfName);
	}

	/**
	 * 従業員を追加します.
	 * 
	 * @param employee 追加する従業員情報
	 */
	public void insert(Employee employee) {
		employeeRepository.insert(employee);
	}

	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee 更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}

	/**
	 * 画像をBase64の文字列に変換します.
	 * 
	 * @param multiFile 画像
	 * @return 返還後の文字列
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public String encode(MultipartFile multiFile) throws IllegalStateException, IOException {
		Path path = savefile(multiFile);
		File file = new File(path.toString());
		multiFile.transferTo(file);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		return Base64.getEncoder().encodeToString(fileContent);
	}

	/**
	 * 新しくアップロードされた画像に付与するファイル名を取得.
	 * 
	 * @return ファイル名
	 */
	public Integer getNewPictureName() {
		return employeeRepository.findMaxPictureId() + 1;
	}

	/**
	 * 新しくアップロードされた画像を保存.
	 * 
	 * @param file アップロードされた画像
	 * @throws IOException 例外
	 */
	public Path savefile(MultipartFile file) throws IOException {
		Path uploadfile = Paths
				.get(System.getProperty("user.dir") + "/src/main/resources/static/img/" + file.getOriginalFilename());

		OutputStream os = Files.newOutputStream(uploadfile, StandardOpenOption.CREATE);
		byte[] bytes = file.getBytes();
		os.write(bytes);
		return uploadfile;
	}
}
