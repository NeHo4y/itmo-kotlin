INSERT INTO USER (ID, CREATED_DATE, EMAIL, IS_DELETED, IS_EMAIL_VALID, PASSWORD, PHONE, ROLE, USERNAME)
VALUES (user_seq.nextval, current_date(), 'admin1@qwe', false, true,
        '$2a$10$NNckB4EA6svjF7MX6kgS0./oGg9bwwZBzotBkKMFOAMzggMzbmo9O', +15551234567, 'ADMIN', 'admin1'),
       (user_seq.nextval, current_date(), 'admin2@qwe', false, true,
        '$2a$10$NNckB4EA6svjF7MX6kgS0./oGg9bwwZBzotBkKMFOAMzggMzbmo9O', +15551234567, 'ADMIN', 'admin2'),
       (user_seq.nextval, current_date(), 'user3@qwe', false, true,
        '$2a$10$NNckB4EA6svjF7MX6kgS0./oGg9bwwZBzotBkKMFOAMzggMzbmo9O', +15551234567, 'USER', 'user3'),
       (user_seq.nextval, current_date(), 'user4@qwe', false, true,
        '$2a$10$NNckB4EA6svjF7MX6kgS0./oGg9bwwZBzotBkKMFOAMzggMzbmo9O', +15551234567, 'USER', 'user4');

INSERT INTO CATEGORY (ID, DESCRIPTION, IS_DELETED)
VALUES (category_seq.nextval, 'Учетная запись', false);

INSERT INTO TOPIC (ID, CATEGORY_ID, DESCRIPTION, IS_DELETED)
VALUES (topic_seq.nextval, category_seq.currval, 'Общие вопросы', false);

INSERT INTO SUBTOPIC (ID, DESCRIPTION, IS_DELETED, TOPIC_ID)
VALUES (subtopic_seq.nextval, 'Нет доступа', false, topic_seq.currval),
       (subtopic_seq.nextval, 'Объединение учетных записей', false, topic_seq.currval);

INSERT INTO TOPIC (ID, CATEGORY_ID, DESCRIPTION, IS_DELETED)
VALUES (topic_seq.nextval, category_seq.currval, 'Магазин', false);

INSERT INTO SUBTOPIC (ID, DESCRIPTION, IS_DELETED, TOPIC_ID)
VALUES (subtopic_seq.nextval, 'Невозможно зайти в магазин', false, topic_seq.currval),
       (subtopic_seq.nextval, 'Ошибка при покупке', false, topic_seq.currval);

INSERT INTO CATEGORY (ID, DESCRIPTION, IS_DELETED)
VALUES (category_seq.nextval, 'Оплата', false);

INSERT INTO TOPIC (ID, CATEGORY_ID, DESCRIPTION, IS_DELETED)
VALUES (topic_seq.nextval, category_seq.currval, 'Авторизация оплаты', false);

INSERT INTO SUBTOPIC (ID, DESCRIPTION, IS_DELETED, TOPIC_ID)
VALUES (subtopic_seq.nextval, 'Неавторизированный платеж', false, topic_seq.currval),
       (subtopic_seq.nextval, 'Возместить платеж', false, topic_seq.currval);

INSERT INTO TOPIC (ID, CATEGORY_ID, DESCRIPTION, IS_DELETED)
VALUES (topic_seq.nextval, category_seq.currval, 'Обновление информации об оплате', false);

INSERT INTO SUBTOPIC (ID, DESCRIPTION, IS_DELETED, TOPIC_ID)
VALUES (subtopic_seq.nextval, 'Невозможно обновить информацию', false, topic_seq.currval),
       (subtopic_seq.nextval, 'Невозможно оплатить картой мир', false, topic_seq.currval);

INSERT INTO FEEDBACK (ID, AUTHOR_ID, CATEGORY_ID, CREATION_DATE, END_DATE, HEADER, IS_ACTUAL, PRIORITY_ID, STATUS_ID, SUBTOPIC_ID, TOPIC_ID, UPDATE_DATE)
VALUES (FEEDBACK_SEQ.nextval, 3, 1, current_date(), current_date(), 'Нет доступа к учетной записи', true, 'LOW', 'OPEN', 1, 1, current_date());

INSERT INTO FEEDBACK_FOLLOWER (ID, FEEDBACK_ID, FOLLOWER_TYPE, USER_ID)
VALUES (FOLLOWER_SEQ.nextval, FEEDBACK_SEQ.currval, 'ASSIGNEE', 2);

INSERT INTO FEEDBACK_MESSAGE  (ID, AUTHOR_ID, IS_DELETED, IS_UNREAD, MESSAGE_DATE, MESSAGE_TEXT, MESSAGE_TYPE, FEEDBACK_ID)
VALUES (FEEDBACK_MESSAGE_SEQ.nextval, 3, false, true, current_date(), 'Не могу получить доступ к учетной записи', 'body', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 2, false, true, current_date(), 'А вы пробовали перезагрузить компьютер?', 'message', FEEDBACK_SEQ.currval);
       (FEEDBACK_MESSAGE_SEQ.nextval, 3, false, true, current_date(), 'Помогло', 'message', FEEDBACK_SEQ.currval);


INSERT INTO FEEDBACK (ID, AUTHOR_ID, CATEGORY_ID, CREATION_DATE, END_DATE, HEADER, IS_ACTUAL, PRIORITY_ID, STATUS_ID, SUBTOPIC_ID, TOPIC_ID, UPDATE_DATE)
VALUES (FEEDBACK_SEQ.nextval, 4, 1, current_date(), current_date(), 'Объединение учетных записей', true, 'MEDIUM', 'OPEN', 2, 1, current_date());

INSERT INTO FEEDBACK_MESSAGE  (ID, AUTHOR_ID, IS_DELETED, IS_UNREAD, MESSAGE_DATE, MESSAGE_TEXT, MESSAGE_TYPE, FEEDBACK_ID)
VALUES (FEEDBACK_MESSAGE_SEQ.nextval, 4, false, true, current_date(), 'Прошу объединить учетные записи
* ЕГГОГ
* ЕГГОГ', 'body', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 1, false, true, current_date(), 'Да кто такой этот ваш еггог?', 'message', FEEDBACK_SEQ.currval);