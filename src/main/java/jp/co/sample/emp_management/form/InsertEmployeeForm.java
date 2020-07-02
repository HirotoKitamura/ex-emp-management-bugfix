package jp.co.sample.emp_management.form;

import java.sql.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

/**
 * 従業員情報の登録フォーム.
 * 
 * @author hiroto.kitamura
 *
 */
public class InsertEmployeeForm {

	/** 従業員名 */
	@NotBlank(message = "名前を入力してください")
	private String name;
	/** 画像 */
	private MultipartFile picture;
	/** 性別 */
	@NotEmpty(message = "性別を選択してください")
	private String gender;
	/** 入社日 */
	private Date hireDate;
	/** メールアドレス */
	@NotBlank(message = "メールアドレスを入力してください")
	private String mailAddress;
	/** 郵便番号 */
	@Pattern(regexp = "^\\d{3}-\\d{4}$", message = "指定の形式で入力してください")
	private String zipCode;
	/** 住所 */
	@NotBlank(message = "住所を入力してください")
	private String address;
	/** 電話番号 */
	@Pattern(regexp = "^0\\d{2,3}-\\d{1,4}-\\d{4}$", message = "指定の形式で入力してください")
	private String telephone;
	/** 給料 */
	@Range(min = 0, max = 100000000, message = "0円から1億円の範囲で入力して下さい")
	private Integer salary;
	/** 特性 */
	@NotBlank(message = "特性を入力してください")
	private String characteristics;
	/** 扶養人数 */
	@Range(min = 0, max = 100000000, message = "0人から1億人の範囲で入力して下さい")
	private Integer dependentsCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getPicture() {
		return picture;
	}

	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}

	public String getGender() {
		if ("male".equals(gender)) {
			return "男性";
		} else {
			return "女性";
		}
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getSalary() {
		return salary == null ? 0 : salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public String getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}

	public Integer getDependentsCount() {
		return dependentsCount == null ? 0 : dependentsCount;
	}

	public void setDependentsCount(Integer dependentsCount) {
		this.dependentsCount = dependentsCount;
	}

	@Override
	public String toString() {
		return "InsertEmployeeForm [name=" + name + ", picture=" + picture + ", gender=" + gender + ", hireDate="
				+ hireDate + ", mailAddress=" + mailAddress + ", zipCode=" + zipCode + ", address=" + address
				+ ", telephone=" + telephone + ", salary=" + salary + ", characteristics=" + characteristics
				+ ", dependentsCount=" + dependentsCount + "]";
	}

}
