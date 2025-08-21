--changeset mykhaylo:create-all-tables

create table users
(
    user_id       bigint generated always as identity primary key,
    name          varchar(255)        not null,
    email         varchar(255) unique not null,
    phone_number  varchar(20)         not null,
    password_hash varchar(100)        not null,
    role          varchar(20)         not null
);

create table cart
(
    cart_id bigint generated always as identity primary key,
    user_id bigint not null references users (user_id)
);

create table categories
(
    category_id bigint generated always as identity primary key,
    name        varchar(255) not null
);

create table products
(
    product_id     bigint generated always as identity primary key,
    name           varchar(255) not null,
    description    varchar(255),
    price          decimal      not null,
    category_id    bigint       not null references categories (category_id),
    image_url      varchar,
    discount_price decimal,
    created_at     timestamp default current_timestamp,
    updated_at     timestamp default current_timestamp
);

create table favorites
(
    favorite_id bigint generated always as identity primary key,
    user_id     bigint not null references users (user_id),
    product_id  bigint not null references products (product_id),
    unique (user_id, product_id)
);

create table cart_items
(
    cart_item_id bigint generated always as identity primary key,
    cart_id      bigint not null references cart (cart_id),
    product_id   bigint not null references products (product_id),
    quantity     bigint not null check (quantity > 0)
);

create table orders
(
    order_id         bigint generated always as identity primary key,
    user_id          bigint          not null references users (user_id),
    created_at       timestamp,
    delivery_address varchar(255) not null,
    contact_phone    varchar(20)  not null,
    delivery_method  varchar(100) not null,
    status           varchar(20)  not null,
    updated_at       timestamp
);

create table order_items
(
    order_item_id     bigint generated always as identity primary key,
    order_id          bigint  not null references orders (order_id),
    product_id        bigint  not null references products (product_id),
    quantity          bigint  not null check ( quantity > 0 ),
    price_at_purchase decimal not null
);

create table payments
(
    id bigint generated always as identity primary key,
    order_id bigint not null references orders (order_id),
    amount numeric(19,2) not null,
    status varchar(20) not null,
    created_at timestamp,
    updated_at timestamp
);
