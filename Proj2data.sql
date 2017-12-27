set serveroutput on
-- drop the data that we currently have with the same names

drop sequence log_sequence;
drop sequence sup_sequence;
drop sequence pur_sequence;

drop trigger insert_customers_trigger;
drop trigger update_last_visit_date_trigger;
drop trigger insert_purchases_trigger;
drop trigger update_qoh_trigger;
drop trigger insert_supplies_trigger;
drop trigger delete_purchases_trigger;

drop package rbms;

drop table purchases;
drop table employees;
drop table customers;
drop table supplies;
drop table products;
drop table discounts;
drop table suppliers;
drop table logs;

-- create tables

create table employees
(eid char(3) primary key,
name varchar2(15),
telephone# char(12),
email varchar2(20));

create table customers
(cid char(4) primary key,
name varchar2(15),
telephone# char(12),
visits_made number(4) check (visits_made >= 1),
last_visit_date date);

create table discounts
(discnt_category number(1) primary key check(discnt_category in (1, 2, 3, 4)),
discnt_rate number(3,2) check (discnt_rate between 0 and 0.8));

create table products
(pid char(4) primary key,
name varchar2(15),
qoh number(5),
qoh_threshold number(4),
original_price number(6,2),
discnt_category number(1) references discounts);

create table purchases
(pur# number(6) primary key,
eid char(3) references employees(eid),
pid char(4) references products(pid),
cid char(4) references customers(cid),
qty number(5),
ptime date,
total_price number(7,2));

create table suppliers
(sid char(2) primary key,
name varchar2(15) not null unique,
city varchar2(15),
telephone# char(12) not null unique,
email varchar2(20) unique);

create table supplies
(sup# number(4) primary key,
pid char(4) references products(pid),
sid char(2) references suppliers(sid),
sdate date,
quantity number(5),
unique(pid, sid, sdate));

create table logs
(log# number(5) primary key,
user_name varchar2(12) not null,
operation varchar2(6) not null,
op_time date not null,
table_name varchar2(20) not null,
tuple_pkey varchar2(6)); 

-- insert data into tables

insert into employees values ('e01', 'David', '666-555-1234', 'david@rb.com');
insert into employees values ('e02', 'Peter', '777-555-2341', 'peter@rb.com');
insert into employees values ('e03', 'Susan', '888-555-3412', 'susan@rb.com');
insert into employees values ('e04', 'Anne', '666-555-4123', 'anne@rb.com');
insert into employees values ('e05', 'Mike', '444-555-4231', 'mike@rb.com');

insert into customers values ('c001', 'Kathy', '666-555-4567', 3, '12-OCT-17');
insert into customers values ('c002', 'John', '888-555-7456', 1, '08-OCT-17');
insert into customers values ('c003', 'Chris', '666-555-6745', 3, '18-SEP-17');
insert into customers values ('c004', 'Mike', '999-555-5674', 1, '15-OCT-17');
insert into customers values ('c005', 'Mike', '777-555-4657', 2, '30-AUG-17');
insert into customers values ('c006', 'Connie', '777-555-7654', 2, '16-OCT-17');
insert into customers values ('c007', 'Katie', '888-555-6574', 1, '12-OCT-17');
insert into customers values ('c008', 'Joe', '666-555-5746', 1, '14-OCT-17');

insert into discounts values (1, 0.1);
insert into discounts values (2, 0.2);
insert into discounts values (3, 0.3);
insert into discounts values (4, 0.4);

insert into products values ('p001', 'stapler', 60, 20, 9.99, 1);
insert into products values ('p002', 'TV', 6, 5, 249, 2);
insert into products values ('p003', 'camera', 20, 5, 148, 2);
insert into products values ('p004', 'pencil', 100, 10, 0.99, 1);
insert into products values ('p005', 'chair', 10, 8, 12.98, 3);
insert into products values ('p006', 'lamp', 10, 6, 19.95, 1);
insert into products values ('p007', 'tablet', 50, 10, 149, 2);
insert into products values ('p008', 'computer', 5, 3, 499, 3);
insert into products values ('p009', 'powerbank', 20, 5, 49.95, 1);

insert into purchases values (100001, 'e01', 'p002', 'c001', 1, to_date('12-AUG-2017 10:34:30', 'DD-MON-YYYY HH24:MI:SS'), 211.65);
insert into purchases values (100002, 'e01', 'p003', 'c001', 1, to_date('20-SEP-2017 11:23:36', 'DD-MON-YYYY HH24:MI:SS'), 118.40);
insert into purchases values (100003, 'e02', 'p004', 'c002', 5, to_date('08-OCT-2017 09:30:50', 'DD-MON-YYYY HH24:MI:SS'), 4.95);
insert into purchases values (100004, 'e01', 'p005', 'c003', 2, to_date('23-AUG-2017 16:23:35', 'DD-MON-YYYY HH24:MI:SS'), 18.17);
insert into purchases values (100005, 'e04', 'p007', 'c004', 1, to_date('15-OCT-2017 13:38:55', 'DD-MON-YYYY HH24:MI:SS'), 119.20);
insert into purchases values (100006, 'e03', 'p008', 'c001', 1, to_date('12-OCT-2017 15:22:10', 'DD-MON-YYYY HH24:MI:SS'), 349.30);
insert into purchases values (100007, 'e03', 'p006', 'c003', 2, to_date('10-SEP-2017 17:12:20', 'DD-MON-YYYY HH24:MI:SS'), 35.91);
insert into purchases values (100008, 'e03', 'p006', 'c005', 1, to_date('16-AUG-2017 12:22:15', 'DD-MON-YYYY HH24:MI:SS'), 17.96);
insert into purchases values (100009, 'e03', 'p001', 'c007', 1, to_date('12-OCT-2017 14:44:23', 'DD-MON-YYYY HH24:MI:SS'), 8.99);
insert into purchases values (100010, 'e04', 'p002', 'c006', 1, to_date('19-SEP-2017 17:32:37', 'DD-MON-YYYY HH24:MI:SS'), 211.65);
insert into purchases values (100011, 'e02', 'p004', 'c006', 10, to_date('16-OCT-2017 16:54:40', 'DD-MON-YYYY HH24:MI:SS'), 9.90);
insert into purchases values (100012, 'e02', 'p008', 'c003', 2, to_date('18-SEP-2017 15:56:38', 'DD-MON-YYYY HH24:MI:SS'), 698.60);
insert into purchases values (100013, 'e04', 'p006', 'c005', 2, to_date('30-AUG-2017 10:38:25', 'DD-MON-YYYY HH24:MI:SS'), 35.91);
insert into purchases values (100014, 'e03', 'p009', 'c008', 3, to_date('14-OCT-2017 10:54:06', 'DD-MON-YYYY HH24:MI:SS'), 134.84);

insert into suppliers values ('s1', 'SuperSupp', 'Vestal', '666-555-3333', 'supersupp@rb.com');
insert into suppliers values ('s2', 'HaveItAll', 'Binghamton', '666-555-4444', 'haveitall@rb.com');
insert into suppliers values ('s3', 'BigStore', 'Vestal', '666-555-7777', 'bigstore@rb.com');

insert into supplies values (1000, 'p001', 's1', '20-AUG-17', 61);
insert into supplies values (1001, 'p002', 's1', '02-AUG-17', 8);
insert into supplies values (1002, 'p003', 's1', '14-SEP-17', 21);
insert into supplies values (1003, 'p004', 's2', '05-OCT-17', 115);
insert into supplies values (1004, 'p005', 's2', '06-AUG-17', 8);
insert into supplies values (1005, 'p006', 's2', '10-AUG-17', 15);
insert into supplies values (1006, 'p007', 's3', '15-OCT-17', 51);
insert into supplies values (1007, 'p008', 's3', '12-OCT-17', 8);
insert into supplies values (1008, 'p009', 's3', '14-OCT-17', 23);
insert into supplies values (1009, 'p005', 's3', '23-AUG-17', 4);

-- create the sequences

create sequence sup_sequence start with 1010 increment by 1 order;
create sequence pur_sequence start with 100015 increment by 1 order;
create sequence log_sequence start with 10001 increment by 1 order;

-- create trigger to insert into logs after insert on customers

create or replace trigger insert_customers_trigger
after insert on customers
for each row

begin
	insert into logs values
	(log_sequence.nextval, user, 'insert', sysdate, 'customers', :NEW.cid);
end;
/

-- create trigger to insert into logs after update of last_visit_date on customers

create or replace trigger update_last_visit_date_trigger
after update of last_visit_date on customers
for each row

begin
	insert into logs values
	(log_sequence.nextval, user, 'update', sysdate, 'customers', :NEW.cid);	
end;
/
show errors

 -- create trigger to insert into logs after insert on purchases

create or replace trigger insert_purchases_trigger
after insert on purchases
for each row

declare
	pid_new products.pid%type;
	pr_row products%rowtype;
    sup_count number(3);
    sid_in supplies.sid%type;
	new_qty supplies.quantity%type;
	M supplies.quantity%type;
    new_qoh products.qoh%type;
    
    no_supplier_found exception;
    
    cid_new customers.cid%type;
    visits_made_new customers.visits_made%type;
    last_visit_date_new customers.last_visit_date%type;

	
begin
	pid_new := :NEW.pid;
	dbms_output.put_line('product ' || pid_new || ' purchased');
	
	insert into logs values
	(log_sequence.nextval, user, 'insert', sysdate, 'purchases', :NEW.pur#);
	
	select * into pr_row
	from products pr where pr.pid = pid_new;
	
	update products
	set qoh = (pr_row.qoh - :NEW.qty)
	where pid = pid_new;
	
	select * into pr_row
	from products pr where pr.pid = pid_new;
    
    -- put a message that qoh is below the required threshold
    
    if(pr_row.qoh < pr_row.qoh_threshold) then
        dbms_output.put_line('Current qoh of the product is below the required threshold and new supply is required!');
			
		select count(*) into sup_count
		from supplies su
		where su.pid = pr_row.pid;
		
		if(sup_count < 1) then
			raise no_supplier_found;
		end if;
		
		select su.sid into sid_in
		from supplies su
		where su.pid = pr_row.pid and rownum = 1
		order by su.sid;
		
		M := pr_row.qoh_threshold - pr_row.qoh + 1;
		new_qty := 10 + M + pr_row.qoh;
		
        -- insert into supplies as we get supply of the product which has low qoh
        
		insert into supplies values
		(sup_sequence.nextval, pr_row.pid, sid_in, sysdate, new_qty);
        
        update products
        set qoh = (pr_row.qoh + new_qty)
        where pid = pid_new;
        
        select qoh into new_qoh
		from products
		where pid = pid_new;
        
        dbms_output.put_line('Updated qoh of the product is: ' || new_qoh);
		
    end if;
    
    select c.cid, visits_made, last_visit_date
	into cid_new, visits_made_new, last_visit_date_new
	from customers c
	where :NEW.cid = c.cid;
	
	if(to_char(last_visit_date_new , 'DD-MON-YY') != to_char(:NEW.ptime, 'DD-MON-YY')) then 
		update customers
		set visits_made = visits_made_new + 1
		where cid = cid_new;
	
		update customers
		set last_visit_date = :NEW.ptime
		where cid = cid_new;
	end if; 
        
exception
    when no_supplier_found then
        dbms_output.put_line('No supplier found for this product!');
        raise_application_error(-20129, 'No supplier found for this product!');
	
end;
/
show errors

-- create trigger to insert into logs after update of qoh on products

create or replace trigger update_qoh_trigger
after update of qoh on products
for each row

begin
	insert into logs values
	(log_sequence.nextval, user, 'update', sysdate, 'products', :NEW.pid);
end;
/

create or replace trigger insert_supplies_trigger
after insert on supplies
for each row

begin
	insert into logs values
	(log_sequence.nextval, user, 'insert', sysdate, 'supplies', :NEW.sup#);
end;
/
show errors

-- create trigger to insert into logs after delete on purchases

create or replace trigger delete_purchases_trigger
after delete on purchases
for each row

begin
    
    update products
        set products.qoh = products.qoh + :OLD.qty
        where products.pid = :OLD.pid;
        
    update customers
        set visits_made = visits_made + 1,
        last_visit_date = sysdate
        where cid = :OLD.cid;

end;
/
show errors