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