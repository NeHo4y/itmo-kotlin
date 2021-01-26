INSERT INTO USER (ID, CREATED_DATE, EMAIL, IS_DELETED, IS_EMAIL_VALID, PASSWORD, PHONE, ROLE, USERNAME)
VALUES (user_seq.nextval, CURRENT_TIME(), 'admin1@qwe', false, true,
        '$2a$10$NNckB4EA6svjF7MX6kgS0./oGg9bwwZBzotBkKMFOAMzggMzbmo9O', +15551234567, 'ADMIN', 'admin1'),
       (user_seq.nextval, CURRENT_TIME(), 'admin2@qwe', false, true,
        '$2a$10$NNckB4EA6svjF7MX6kgS0./oGg9bwwZBzotBkKMFOAMzggMzbmo9O', +15551234567, 'ADMIN', 'admin2'),
       (user_seq.nextval, CURRENT_TIME(), 'user3@qwe', false, true,
        '$2a$10$NNckB4EA6svjF7MX6kgS0./oGg9bwwZBzotBkKMFOAMzggMzbmo9O', +15551234567, 'USER', 'user3'),
       (user_seq.nextval, CURRENT_TIME(), 'user4@qwe', false, true,
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
VALUES (FEEDBACK_SEQ.nextval, 3, 1, CURRENT_TIME(), CURRENT_TIME(), 'Нет доступа к учетной записи', true, 'LOW', 'RESOLVED', 1, 1, CURRENT_TIME());

INSERT INTO FEEDBACK_FOLLOWER (ID, FEEDBACK_ID, FOLLOWER_TYPE, USER_ID)
VALUES (FOLLOWER_SEQ.nextval, FEEDBACK_SEQ.currval, 'ASSIGNEE', 2);

INSERT INTO FEEDBACK_MESSAGE  (ID, AUTHOR_ID, IS_DELETED, IS_UNREAD, MESSAGE_DATE, MESSAGE_TEXT, MESSAGE_TYPE, FEEDBACK_ID)
VALUES (FEEDBACK_MESSAGE_SEQ.nextval, 3, false, true, CURRENT_TIME(), 'Не могу получить доступ к учетной записи', 'body', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 2, false, true, CURRENT_TIME(), 'А вы пробовали перезагрузить компьютер?', 'message', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 3, false, true, CURRENT_TIME(), 'Помогло', 'message', FEEDBACK_SEQ.currval);


INSERT INTO FEEDBACK (ID, AUTHOR_ID, CATEGORY_ID, CREATION_DATE, END_DATE, HEADER, IS_ACTUAL, PRIORITY_ID, STATUS_ID, SUBTOPIC_ID, TOPIC_ID, UPDATE_DATE)
VALUES (FEEDBACK_SEQ.nextval, 3, 2, CURRENT_TIME(), CURRENT_TIME(), 'Назначьте меня на админа', true, 'MEDIUM', 'CREATED', 5, 3, CURRENT_TIME());

INSERT INTO FEEDBACK_MESSAGE  (ID, AUTHOR_ID, IS_DELETED, IS_UNREAD, MESSAGE_DATE, MESSAGE_TEXT, MESSAGE_TYPE, FEEDBACK_ID)
VALUES (FEEDBACK_MESSAGE_SEQ.nextval, 3, false, true, CURRENT_TIME(), 'Да когда мой отзыв возьмут уже в работу???', 'body', FEEDBACK_SEQ.currval);


INSERT INTO FEEDBACK (ID, AUTHOR_ID, CATEGORY_ID, CREATION_DATE, END_DATE, HEADER, IS_ACTUAL, PRIORITY_ID, STATUS_ID, SUBTOPIC_ID, TOPIC_ID, UPDATE_DATE)
VALUES (FEEDBACK_SEQ.nextval, 4, 1, CURRENT_TIME(), CURRENT_TIME(), 'Объединение учетных записей', true, 'MEDIUM', 'IN_PROGRESS', 2, 1, CURRENT_TIME());

INSERT INTO FEEDBACK_FOLLOWER (ID, FEEDBACK_ID, FOLLOWER_TYPE, USER_ID)
VALUES (FOLLOWER_SEQ.nextval, FEEDBACK_SEQ.currval, 'ASSIGNEE', 1);

INSERT INTO FEEDBACK_MESSAGE  (ID, AUTHOR_ID, IS_DELETED, IS_UNREAD, MESSAGE_DATE, MESSAGE_TEXT, MESSAGE_TYPE, FEEDBACK_ID)
VALUES (FEEDBACK_MESSAGE_SEQ.nextval, 4, false, true, CURRENT_TIME(), 'Прошу объединить учетные записи
* ЕГГОГ
* ЕГГОГ', 'body', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 1, false, true, CURRENT_TIME(), 'Да кто такой этот ваш еггог?', 'message', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 4, false, true, CURRENT_TIME(), '> Да кто такой этот ваш еггог?
Лучше вам и не знать, вы молодой, шутливый', 'message', FEEDBACK_SEQ.currval);