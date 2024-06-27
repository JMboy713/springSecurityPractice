Create Table if not exists 'spring'.'user' (
    username varchar(255) not null,
    password varchar(255) not null,
    PRIMARY KEY (username)
);
create table if not exists 'spring'.'otp' (
    username varchar(255) not null,
    code varchar(45) null,
    PRIMARY KEY (username)
);
