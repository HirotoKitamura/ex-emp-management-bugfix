package jp.co.sample.emp_management.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.domain.AuthAdmin;
import jp.co.sample.emp_management.form.InsertAdministratorForm;
import jp.co.sample.emp_management.form.LoginForm;
import jp.co.sample.emp_management.service.AdministratorService;

/**
 * 管理者情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/")
public class AdministratorController {

	@Autowired
	private AdministratorService administratorService;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public InsertAdministratorForm setUpInsertAdministratorForm() {
		return new InsertAdministratorForm();
	}

	// (SpringSecurityに任せるためコメントアウトしました)
	@ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：管理者を登録する
	/////////////////////////////////////////////////////
	/**
	 * 管理者登録画面を出力します.
	 * 
	 * @return 管理者登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		return "administrator/insert";
	}

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param form   管理者情報用フォーム
	 * @param result 入力値チェック結果
	 * @param model  リクエストスコープ
	 * @return ログイン画面へリダイレクト 入力値チェックに引っかかった場合はこの画面に留まる
	 */
	@RequestMapping("/insert")
	public String insert(@Validated InsertAdministratorForm form, BindingResult result, Model model) {
		if (administratorService.isExistingMailAddress(form.getMailAddress())) {
			result.rejectValue("mailAddress", "mailAddressExists", "このメールアドレスは既に登録されています");
		}
		if (!"".equals(form.getConfirmPassword()) && !form.getPassword().equals(form.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "passwordNotMatch", "確認用パスワードが一致しません");
		}
		if (result.hasErrors()) {
			return "administrator/insert";
		}
		Administrator administrator = new Administrator();
		// フォームからドメインにプロパティ値をコピー
		BeanUtils.copyProperties(form, administrator);
		administratorService.insert(administrator);
		return "redirect:/";
	}

	/////////////////////////////////////////////////////
	// ユースケース：ログインをする
	/////////////////////////////////////////////////////

	/**
	 * ログイン画面に飛ばす
	 * 
	 * @return ログイン画面
	 */
	@RequestMapping("")
	public String index() {
		return "redirect:/login";
	}

	/**
	 * ログインします.
	 * 
	 * @param form   管理者情報用フォーム
	 * @param result エラー情報格納用オブジェクト
	 * @param model  リクエストスコープ
	 * @return ログイン後の従業員一覧画面
	 */
	@RequestMapping("/login")
	public String login(LoginForm form, Model model, Boolean error, @AuthenticationPrincipal AuthAdmin admin) {
//		Administrator administrator = administratorService.login(form.getMailAddress(), form.getPassword());
		if (error != null && error) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。");
			return "administrator/login";
		}
		System.out.println(admin == null ? "ないよー" : admin.getUsername());
//		session.setAttribute("administratorName", administrator.getName());
		return "administrator/login";
	}

	/////////////////////////////////////////////////////
	// ユースケース：ログアウトをする
	/////////////////////////////////////////////////////
	/**
	 * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
	 * 
	 * @return ログイン画面
	 */
//	@RequestMapping(value = "/logout")
//	public String logout() {
//		session.invalidate();
//		return "redirect:/login";
//	}

}
