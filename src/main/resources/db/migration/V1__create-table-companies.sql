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