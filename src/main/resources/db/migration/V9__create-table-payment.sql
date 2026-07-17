create table payments(
	id bigint auto_increment primary key,

	amount decimal(10,2) not null,
	payment_method varchar(50) not null,
	payment_status varchar(50) not null,

    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,
    canceled_at timestamp null default null,
    paid_at timestamp null default null,


    company_id bigint not null,
    service_order_id bigint not null,


    constraint fk_payment_company foreign key (company_id) references companies(id),
    constraint fk_payment_service_order foreign key (service_order_id) references service_orders(id)

);