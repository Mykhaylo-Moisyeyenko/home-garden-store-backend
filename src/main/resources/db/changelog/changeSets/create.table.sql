
--changeset mykhaylo:create-all-tables
create type user_role as enum ('ROLE_USER', 'ROLE_ADMINISTRATOR');

create table users
(
    user_id       int generated always as identity primary key,
    name          varchar(255)        not null,
    email         varchar(255) unique not null,
    phone_number  varchar(20)         not null,
    password_hash varchar(100)        not null,
    role          user_role           not null
);

create table cart
(
    cart_id int generated always as identity primary key,
    user_id int not null references users (user_id)
);

create table categories
(
    category_id int generated always as identity primary key,
    name        varchar(255) not null
);

create table products
(
    product_id     int generated always as identity primary key,
    name           varchar(255)  not null,
    description    varchar(255),
    price          decimal(6, 2) not null,
    category_id    int           not null references categories (category_id),
    image_url      varchar,
    discount_price decimal(6, 2),
    created_at     timestamp default current_timestamp,
    updated_at     timestamp default current_timestamp
);

create table favorites
(
    favorite_id int generated always as identity primary key,
    user_id     int not null references users (user_id),
    product_id  int not null references products (product_id),
    unique (user_id, product_id)
);

create table cart_items
(
    cart_item_id int generated always as identity primary key,
    cart_id      int not null references cart (cart_id),
    product_id   int not null references products (product_id),
    quantity     int not null check (quantity > 0)
);

create type order_status as enum (
    'CREATED',
    'AWAITING_PAYMENT',
    'PAID',
    'SHIPPED',
    'DELIVERED',
    'CANCELLED'
);

create table orders
(
    order_id         int generated always as identity primary key,
    user_id          int          not null references users (user_id),
    created_at       timestamp             default current_timestamp,
    delivery_address varchar(255) not null,
    contact_phone    varchar(20)  not null,
    delivery_method  varchar(100) not null,
    status           order_status not null default 'CREATED',
    updated_at       timestamp             default current_timestamp
);

create table order_items
(
    order_item_id     int generated always as identity primary key,
    order_id          int           not null references orders (order_id),
    product_id        int           not null references products (product_id),
    quantity          int           not null check ( quantity > 0 ),
    price_at_purchase decimal(6, 2) not null
);
