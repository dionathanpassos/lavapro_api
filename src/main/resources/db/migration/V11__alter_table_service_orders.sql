create table cash_flows(
	id bigint auto_increment primary key,

	type varchar(50) not null,
	category varchar(50) not null,
	amount decimal(10,2) not null,

    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,

    company_id bigint not null,
    service_order_id bigint not null,
    payment_id bigint not null,

    constraint fk_cash_flow_company foreign key (company_id) references companies(id),
    constraint fk_cash_flow_service_order foreign key (service_order_id) references service_orders(id),
    constraint fk_cash_flow_payment foreign key (payment_id) references payments(id)
);