--Este script contine la tabla necesaria para la demo
create table PLANETA (
    id_planeta numeric(8) primary key,
    nombre varchar(50),
    diametro numeric(8),
    tipo varchar(15),
    observaciones varchar(50)
);

insert into PLANETA values (10,'Mercurio',4879,'rocoso','mensajero de los dioses');
insert into PLANETA values (20,'Venus',12103,'telurico','diosa del amor y de la belleza');
insert into PLANETA values (30,'Tierra',12875,'telurico','madre de todos los dioses');
insert into PLANETA values (40,'Martes',6794,'telurico','dios de la guerra');
insert into PLANETA values (50,'Jupiter',142984,'gaseoso','dios supremo y creador del universo');
insert into PLANETA values (60,'Saturno',119300,'gaseoso','dios titan y padreo de Jupiter');
insert into PLANETA values (70,'Urano',51118,'gaseoso','dios del cielo');
insert into PLANETA values (80,'Neptuno',49572,'gaseoso','dios del mar');
