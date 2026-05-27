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