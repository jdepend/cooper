--common

drop table analysissummry;

create table analysissummry (
id                   varchar(36)                    not null,
client               varchar(20),
username             varchar(32),
groupname            varchar(32),
commandname          varchar(64),
LC                   integer,
CN                   integer,
CC                   integer,
AC                   integer,
Ca                   integer,
Ce                   integer,
A                    float,
I                    float,
D                    float,
Coupling             float,
Cohesion             float,
Balance              float,
Encapsulation        float,
OO                   float,
UC                   int,
createdate           timestamp                       default now(),
primary key (id)
);


drop table analysisdata;

create table analysisdata (
id                   varchar(36)                    not null,
result               blob,
primary key (id)
);



--client

drop table score;

create table score (
id                   varchar(36)                    not null,
groupname            varchar(32),
commandname          varchar(64),
LC                   integer,
componentCount       integer,
relationCount        integer,
Score                Float,
Distance             float,
Balance              float,
Relation             float,
Encapsulation        float,
cohesion             float,
coupling             float,
createdate           timestamp                       default now(),
primary key (id)
);



drop table scoredata;

create table scoredata (
id                   varchar(36)                    not null,
result               blob,
primary key (id)
);

drop table scoreext;

create table scoreext (
id                   VARCHAR(36)                    not null,
scoreid              VARCHAR(36),
itemname             VARCHAR(32),
score                float,
primary key (id)
);

drop table busilog;

create table busilog (
id                   varchar(36)                    not null,
username             varchar(32),
operation            varchar(64),
createdate           timestamp                       default now(),
primary key (id)
);


--server

drop table analyzer;

create table analyzer (
classname            varchar(256),
name                 varchar(256)                   not null,
tip                  varchar(256),
bigtip               varchar(1024),
type                 varchar(32),
defaultdata          blob,
def                  blob,
username             varchar(256),
createdate           timestamp,
uploaddate           timestamp                       default now(),
primary key (name)
);

drop table scorelist;

create table scorelist (
id                   varchar(36)                    not null,
ip                   varchar(32),
username             varchar(32),
groupname            varchar(32),
commandname          varchar(64),
LC                   integer,
componentCount       integer,
relationCount        integer,
Score                Float,
Distance             float,
Balance              float,
Relation             float,
Encapsulation        float,
cohesion             float,
coupling             float,
createdate           timestamp,
uploaddate           timestamp                       default now(),
primary key (id)
);

drop table user;

create table user (
name                 varchar(36)                    not null,
password             varchar(32),
dept                 varchar(32),
integral             int,
valid                char(1),
primary key (name)
);

drop table useraction;

create table useraction (
id                   varchar(36)                    not null,
username             varchar(32),
operation            varchar(64),
ip                   varchar(32),
createdate           timestamp,
gartherdate          timestamp                       default now(),
primary key (id)
);


insert into user values('admin', null, null, null,'Y');
insert into user values('AnonymousUser', null, null, 0, 'Y');
insert into user values('test', 'test', null, 1000, 'Y');




