package com.xhwl.licensePlate.huawei.items;

public class VatInvoiceListItem {
	private String name;
	private String specification;
	private String unit;
	private String quantity;
	private String unit_price;
	private String license_plate_number;
	private String vehicle_type;
	private String start_date;
	private String end_date;
	private String amount;
	private String tax_rate;
	private String tax;

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getSpecification() {
		return specification;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnit() {
		return unit;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
	public String getUnit_price() {
		return unit_price;
	}
	public void setLicense_plate_number(String license_plate_number) {
		this.license_plate_number = license_plate_number;
	}
	public String getLicense_plate_number() {
		return license_plate_number;
	}
	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}
	public String getVehicle_type() {
		return vehicle_type;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAmount() {
		return amount;
	}
	public void setTax_rate(String tax_rate) {
		this.tax_rate = tax_rate;
	}
	public String getTax_rate() {
		return tax_rate;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getTax() {
		return tax;
	}

	@Override
	public String toString() {
		return "com.ocr.items.VatInvoiceListItem{" +
				"name=\'" + name + "\'," +
				"specification=\'" + specification + "\'," +
				"unit=\'" +unit + "\'," +
				"quantity=\'" + quantity + "\'," +
				"unit_price=\'" + unit_price + "\'," +
				"license_plate_number=\'" +license_plate_number + "\'," +
				"vehicle_type=\'" + vehicle_type + "\'," +
				"start_date=\'" + start_date + "\'," +
				"end_date=\'" + end_date +"\'," +
				"amount=\'" + amount + "\'," +
				"tax_rate=\'" + tax_rate + "\'," +
				"tax=\'" + tax + "\'" +
				"}";
	}
}
