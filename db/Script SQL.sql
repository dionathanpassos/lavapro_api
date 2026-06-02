use teste;
show tables from lavapro_api;
select * from customers;




create table users (
	id bigint auto_increment primary key,
    name varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    role varchar(125) not null,
    active boolean not null default true,
    
    created_at timestamp default current_timestamp not null,
    update_at timestamp default current_timestamp on update current_timestamp not null,
    deleted_at timestamp,
    
    company_id bigint not null,
    
    constraint fk_users_company foreign key (company_id) references companies(id),
    constraint uq_users_company_email unique(company_id, email)
    
);

create table companies(
	id bigint auto_increment primary key,
    name varchar(255) not null,
    trade_name varchar(255),
    document varchar(125),
    phone varchar(255),
    business_email varchar(255) not null,
    status varchar(125) not null,
    
    created_at timestamp default current_timestamp not null,
    update_at timestamp default current_timestamp on update current_timestamp not null,
    deleted_at timestamp
    
);

create table customers(
	id bigint auto_increment primary key,
    name varchar(255) not null,
    phone varchar(20) not null,
    
    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    deleted_at timestamp null,
    
    company_id bigint not null,
    
    constraint fk_customers_company foreign key (company_id) references companies(id),
    constraint uq_customers_company_phone unique(company_id, phone)
    
);

create table vehicles(
	id bigint auto_increment primary key,
    plate varchar(15) not null,
    brand varchar(30) not null,
    model varchar(30) not null,
    color varchar(30),
    year integer,
    company_id bigint not null,
    customer_id bigint not null,
    
	created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    deleted_at timestamp null,
    
    constraint fk_vehicle_company foreign key (company_id) references companies(id),
	constraint fk_vehicle_customer foreign key (customer_id) references customers(id),
    constraint uk_vehicle_company_plate unique (company_id, plate)
);

create table service_orders(
	id bigint auto_increment primary key,

    status varchar(30) not null,
    total_amount DECIMAL(10,2) not null,
    observations varchar(500),
        
    company_id bigint not null,
    vehicle_id bigint not null,

	created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    deleted_at timestamp null,

    constraint chk_service_order_total_amount check (total_amount >= 0),
    constraint fk_service_order_company foreign key (company_id) references companies(id),
    constraint fk_service_order_vehicle foreign key (vehicle_id) references vehicles(id)
);