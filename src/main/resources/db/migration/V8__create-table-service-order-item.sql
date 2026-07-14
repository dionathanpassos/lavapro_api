create table service_order_item(
	id bigint auto_increment primary key,

    quantity integer not null,
    unit_price DECIMAL(10,2) not null,
    total_price DECIMAL(10,2) not null,
    service_name varchar(120) not null,

    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp on update current_timestamp not null,

    company_id bigint not null,
    service_catalog_id bigint not null,
    service_order_id bigint not null,

    constraint fk_service_order_item_company foreign key (company_id) references companies(id),
    constraint fk_service_order_item_service_catalog foreign key (service_catalog_id) references service_catalog(id),
    constraint fk_service_order_item_service_order foreign key (service_order_id) references service_orders(id),

    constraint chk_service_order_item_quantity check (quantity > 0),
    constraint chk_service_order_item_unit_price check (unit_price >= 0),
	constraint chk_service_order_item_total_price check (total_price >= 0),

    INDEX idx_service_order_item_order (service_order_id),
    INDEX idx_service_order_item_company (company_id)
);