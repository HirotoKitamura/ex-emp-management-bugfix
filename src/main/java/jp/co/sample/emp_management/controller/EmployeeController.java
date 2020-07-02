package jp.co.sample.emp_management.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.InsertEmployeeForm;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpUpdateForm() {
		return new UpdateEmployeeForm();
	}

	@ModelAttribute
	public InsertEmployeeForm setUpInsertForm() {
		return new InsertEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/showList")
	public String showList(Model model) {
		List<Employee> employeeList = employeeService.showList();
		model.addAttribute("employeeList", employeeList);
		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員を検索する
	/////////////////////////////////////////////////////
	/**
	 * 従業員を名前から部分一致検索し、結果の一覧画面を出力します.
	 * 
	 * 空欄(スペース含む)で検索した場合は全件検索、結果が1件もない場合はエラーメッセージを出した上で全件検索をします
	 * 
	 * @param partOfName 入力された文字列
	 * @param model      モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/searchEmployee")
	public String searchEmployee(String partOfName, Model model) {
		if (partOfName == null) {
			return "redirect:/employee/showList";
		}
		model.addAttribute("isResultScreen", true);
		List<Employee> employeeList = employeeService.searchByPartOfName(partOfName);
		model.addAttribute("employeeList", employeeList);
		if (employeeList.size() == 0) {
			model.addAttribute("hasNoResult", true);
			model.addAttribute("employeeList", employeeService.showList());
		}
		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@RequestMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員を登録する
	/////////////////////////////////////////////////////
	/**
	 * 従業員登録画面を出力します.
	 * 
	 * @return 従業員登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		return "employee/insert";
	}

	/**
	 * 従業員情報を登録します.
	 * 
	 * @param form   従業員情報用フォーム
	 * @param result 入力値チェック結果
	 * @param model  リクエストスコープ
	 * @return 従業員一覧画面へリダイレクト 入力値チェックに引っかかった場合はこの画面に留まる
	 * @throws IOException
	 */
	@RequestMapping("/insert")
	synchronized public String insert(@Validated InsertEmployeeForm form, BindingResult result, Model model)
			throws IOException {
		MultipartFile picture = form.getPicture();
		if (picture.isEmpty()) {
			result.rejectValue("picture", null, "画像をアップロードしてください");
		}
		if (picture.getOriginalFilename().endsWith(".png") && !picture.getOriginalFilename().endsWith(".jpg")) {
			result.rejectValue("picture", null, "画像形式はJPGかPNGに限ります");
		}
		if (result.hasErrors()) {
			return "employee/insert";
		}
		Employee employee = new Employee();
		// フォームからドメインにプロパティ値をコピー
		BeanUtils.copyProperties(form, employee);
		if (picture.getOriginalFilename().endsWith(".jpg")) {
			employee.setImage("data:image/jpg;base64," + employeeService.encode(picture));
		} else {
			employee.setImage("data:image/png;base64," + employeeService.encode(picture));
		}
		employeeService.insert(employee);
		return "redirect:/employee/showList";
	}
}
