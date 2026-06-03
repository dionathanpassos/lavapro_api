create table service_catalog(
	id bigint auto_increment primary key,

    name varchar(120) not null,
    price decimal(10,2) not null,
    type varchar(100) not null,
    active boolean default true not null,
    description varchar(500),

    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    deleted_at timestamp null,

    company_id bigint not null,

    constraint chk_service_catalog_price check (price >= 0),
    constraint fk_service_catalog_company foreign key (company_id) references companies(id),
    constraint uk_service_catalog_company_name unique (company_id, name)

);