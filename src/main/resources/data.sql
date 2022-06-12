/*Spring Boot가 DataSource의 schema(ddl scripts)를 생성한 다음
, 초기 데이터를 생성(dml scripts)하기 위해서 schema.sql, data.sql 사용하도록 설정되어 있으며
, 이는 spring.sql.init.platform 을 설정하여 변경하실 수도 있습니다. 기본적으로 JPA에서는 @Entity로 선언한 테이블을 생성 한 다음에
,  초기 데이터를 저장하지 않기 때문에
, data.sql이라는 파일을 classpath 로 지정된 곳에 생성하게 되면
, 초기 부팅시 자동으로 데이터를 저장하게 됩니다.

https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization*/

insert into user values(1, sysdate(),'User1','test1','2222-22');
insert into user values(2, sysdate(),'User2','test2','2222-221');
insert into user values(3, sysdate(),'User3','test3','2222-222');

insert into post values(3, sysdate(),'User3','test3','2222-222');