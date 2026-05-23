create table users (
	id bigint auto_increment primary key,
    name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    role varchar(125) not null,
    active boolean not null default true,

    created_at timestamp default current_timestamp not null,
    update_at timestamp default current_timestamp on update current_timestamp not null,
    deleted_at timestamp,

    company_id bigint not null,

    constraint fk_users_company foreign key (company_id) references companies(id)

);