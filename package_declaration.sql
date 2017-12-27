-- declaration of all the functions and procedures in package

set serveroutput on
create or replace package rbms as
	type ref_cursor_employees is ref cursor;
	type ref_cursor_customers is ref cursor;
	type ref_cursor_products is ref cursor;
	type ref_cursor_discounts is ref cursor;
	type ref_cursor_suppliers is ref cursor;
	type ref_cursor_supplies is ref cursor;
	type ref_cursor_purchases is ref cursor;
	type ref_cursor_logs is ref cursor;
	type ref_cursor_monthly_sale is ref cursor;
	
	function show_employees
	return ref_cursor_employees;
    
	function show_products
	return ref_cursor_products;
    
	function show_customers
	return ref_cursor_customers;
	
    function show_discounts
	return ref_cursor_discounts;
	
    function show_purchases
	return ref_cursor_purchases;
	
    function show_suppliers
	return ref_cursor_suppliers;
	
    function show_supplies
	return ref_cursor_supplies;
	
    function show_logs
	return ref_cursor_logs;
	
	function purchase_saving
	(pur#_in in purchases.pur#%type)
	return number;
	
	procedure monthly_sale_activities
	(eid_in in employees.eid%type,
	rc out ref_cursor_monthly_sale);
	
	procedure add_customer
	(cid_in in customers.cid%type,
	c_name in customers.name%type,
	c_telephone# in customers.telephone#%type);
	
	procedure add_purchase
	(eid_in in purchases.eid%type,
	pid_in in purchases.pid%type,
	cid_in in purchases.cid%type,
	qty_in in purchases.qty%type);
    
    procedure delete_purchase
    (pur#_in in purchases.pur#%type);
	
end rbms;
/
show errors