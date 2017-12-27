-- definition of all the functions and procedures in the package

set serveroutput on
create or replace package body rbms as

	--show functions
	/* Function to show values of employees table */
	function show_employees
	return ref_cursor_employees is rc_e ref_cursor_employees;
	begin
		open rc_e for
		select * from employees;
		return rc_e;
	end;

	/* Function to show values of products table */
	function show_products
	return ref_cursor_products is rc_pr ref_cursor_products;
	begin
		open rc_pr for
		select * from products;
		return rc_pr;
	end;

	/* Function to show values of customers table */
	function show_customers
	return ref_cursor_customers is rc_c ref_cursor_customers;
	begin
        open rc_c for
        select * from customers;
        return rc_c;
	end;
	
	/* Function to show values of discounts table */
    function show_discounts
	return ref_cursor_discounts is rc_d ref_cursor_discounts;
        begin
			open rc_d for
			select * from discounts;
			return rc_d;
        end;

	/* Function to show values of purchases table */
	function show_purchases
	return ref_cursor_purchases is rc_pu ref_cursor_purchases;
	begin
        open rc_pu for
        select * from purchases;
        return rc_pu;
	end;

	/* Function to show values of suppliers table */
	function show_suppliers
	return ref_cursor_suppliers is rc_s ref_cursor_suppliers;
	begin
        open rc_s for
        select * from suppliers;
        return rc_s;
	end;

	/* Function to show values of supplies table */
	function show_supplies
	return ref_cursor_supplies is rc_su ref_cursor_supplies;
	begin
        open rc_su for
        select * from supplies;
        return rc_su;
	end;

	/* Function to show values of logs table */
	function show_logs
	return ref_cursor_logs is rc_l ref_cursor_logs;
	begin
        open rc_l for
        select * from logs;
        return rc_l;
	end;
	
	/*function to calculate purchase saving for any given purchase*/
	function purchase_saving
	(pur#_in in purchases.pur#%type)
	return number is
		total_savings number(7,2);
        
		pur#_is_null exception;
	
	begin
		if pur#_in is NULL then
			raise pur#_is_null;
		end if;

		select(original_price * qty) - total_price as total_savings into total_savings
		from purchases pu, products pr
		where pur# = pur#_in and pu.pid = pr.pid;
	
		return total_savings;
	
	exception
		when pur#_is_null then
			dbms_output.put_line('PUR# is null!');
            raise_application_error(-20111, 'PUR# is null');
		when no_data_found then
			dbms_output.put_line(sqlcode || '--' || sqlerrm);
            raise_application_error(-20112, sqlcode || '--' || sqlerrm);
		when others then
			dbms_output.put_line('SQL exception occured!');
            raise_application_error(-20113, 'SQL exception occured!');
			
	end;
	
	/*procedure to show monthly sale activity for any given employee*/
	procedure monthly_sale_activities
	(eid_in in employees.eid%type, rc out ref_cursor_monthly_sale) is
	
	eid_is_null exception;
	begin
		if eid_in is NULL then
			raise eid_is_null;
		end if;
		
		open rc for
		select e.eid,
			   name,
			   to_char(ptime, 'MON') as Month,
			   to_char(ptime, 'YYYY') as Year,
			   count(pu.eid) as times_sales,
			   sum(qty) as qty_sold,
			   sum(total_price) as total_sold
		from employees e, purchases pu
		where e.eid = eid_in and e.eid = pu.eid
		group by e.eid, name, to_char(ptime, 'MON'), to_char(ptime, 'YYYY');
		
	exception
		when eid_is_null then
			dbms_output.put_line('EID is null');
            raise_application_error(-20114, 'EID is null');
		when no_data_found then
			dbms_output.put_line(sqlcode || '--' || sqlerrm);
            raise_application_error(-20115, sqlcode || '--' || sqlerrm);
		when others then
			dbms_output.put_line('SQL exception occured!');
            raise_application_error(-20116, 'SQL Exception occured!');

	end;
	
	/*procedure to add a customer*/
	procedure add_customer
	(cid_in in customers.cid%type,
	c_name in customers.name%type,
	c_telephone# in customers.telephone#%type) is
	
	cid_is_null exception;
	
	begin
		if cid_in is NULL then
			raise cid_is_null;
		end if;
		
		insert into customers
		values (cid_in, c_name, c_telephone#, 1, SYSDATE);
		
	exception
		when cid_is_null then
			dbms_output.put_line('CID is null!');
            raise_application_error(-20117, 'CID is null!');
		when dup_val_on_index then
			dbms_output.put_line(sqlcode || '--' || sqlerrm);
            raise_application_error(-20118, sqlcode || '--' || sqlerrm);
		when others then
			dbms_output.put_line('SQL exception occured!');
            raise_application_error(-20119, 'SQL Exception occured!');
		
	end;

	/*procedure to add a purchase*/	
	procedure add_purchase
	(eid_in in purchases.eid%type,
	pid_in in purchases.pid%type,
	cid_in in purchases.cid%type,
	qty_in in purchases.qty%type) is
	
	eid_is_null exception;
	pid_is_null exception;
	cid_is_null exception;
	invalid_qty exception;
	insufficient_quantity exception;
		
        
    -- cursors to access dependent tables        
	cursor products_cursor is
		select *
		from products pr;

	cursor discounts_cursor is
		select *
		from discounts d;
    
    products_record products_cursor%rowtype;
    discounts_record discounts_cursor%rowtype;
    
	original_price_in products.original_price%type;
	total_price_in purchases.total_price%type;
    qoh_in products.qoh%type;
	
	begin
		if eid_in is NULL then
			raise eid_is_null;
		end if;
		if pid_in is NULL then
			raise pid_is_null;
		end if;
		if cid_in is NULL then
			raise cid_is_null;
		end if;
		if qty_in is NULL or qty_in < 1 then
			raise invalid_qty;
		end if;
		
        -- access specific attributes from the cursor
        
		for products_record in products_cursor loop
			if(products_record.pid = pid_in) then
				for discounts_record in discounts_cursor loop
					if(products_record.discnt_category = discounts_record.discnt_category) then
                        original_price_in := products_record.original_price * (1 - discounts_record.discnt_rate);
                        total_price_in := original_price_in * qty_in;
                        qoh_in := products_record.qoh;
						exit;
					end if;
				end loop;
			end if;
		end loop;

		if(qoh_in < qty_in) then
			raise insufficient_quantity;
        end if;
			
		insert into purchases values
		(pur_sequence.nextval, eid_in, pid_in, cid_in, qty_in, sysdate, total_price_in);
	
	exception
		when eid_is_null then
			dbms_output.put_line('EID is null!');
            raise_application_error(-20120, 'EID is null!');
		when pid_is_null then
			dbms_output.put_line('PID is null!');
            raise_application_error(-20121, 'PID is null!');
		when cid_is_null then
			dbms_output.put_line('CID is null!');
            raise_application_error(-20122, 'CID is null!');
		when invalid_qty then
			dbms_output.put_line('Invalid quantity!');
            raise_application_error(-20123, 'Invalid quantity!');
		when insufficient_quantity then
			dbms_output.put_line('Insufficient quantity in stock!');
            raise_application_error(-20124, 'Insufficient quantity in stock!');
		when dup_val_on_index then
			dbms_output.put_line(sqlcode || '--' || sqlerrm);
            raise_application_error(-20125, sqlcode || '--' || sqlerrm);
		when no_data_found then
			dbms_output.put_line(sqlcode || '--' || sqlerrm);
            raise_application_error(-20126, sqlcode || '--' || sqlerrm);
		when others then
			dbms_output.put_line('SQL exception occured!');
            raise_application_error(-20127, 'SQL Exception occured!');
	end;
    
   	/*procedure to delete a purchase*/
    procedure delete_purchase
	(pur#_in in purchases.pur#%type) is
	
	pur#_is_null exception;
    
    begin
		if pur#_in is NULL then
			raise pur#_is_null;
        end if;
            
        delete from purchases pu
        where pur#_in = pu.pur#;
        
    exception
        when pur#_is_null then
			dbms_output.put_line('PUR# is null!');
            raise_application_error(-20128, 'PUR# is null!');
    end;
	
end rbms;
/
show errors