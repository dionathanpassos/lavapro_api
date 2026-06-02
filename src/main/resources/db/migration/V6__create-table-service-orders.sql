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