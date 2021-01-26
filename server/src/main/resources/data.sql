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
VALUES (category_seq.nextval, '������� ������', false);

INSERT INTO TOPIC (ID, CATEGORY_ID, DESCRIPTION, IS_DELETED)
VALUES (topic_seq.nextval, category_seq.currval, '����� �������', false);

INSERT INTO SUBTOPIC (ID, DESCRIPTION, IS_DELETED, TOPIC_ID)
VALUES (subtopic_seq.nextval, '��� �������', false, topic_seq.currval),
       (subtopic_seq.nextval, '����������� ������� �������', false, topic_seq.currval);

INSERT INTO TOPIC (ID, CATEGORY_ID, DESCRIPTION, IS_DELETED)
VALUES (topic_seq.nextval, category_seq.currval, '�������', false);

INSERT INTO SUBTOPIC (ID, DESCRIPTION, IS_DELETED, TOPIC_ID)
VALUES (subtopic_seq.nextval, '���������� ����� � �������', false, topic_seq.currval),
       (subtopic_seq.nextval, '������ ��� �������', false, topic_seq.currval);

INSERT INTO CATEGORY (ID, DESCRIPTION, IS_DELETED)
VALUES (category_seq.nextval, '������', false);

INSERT INTO TOPIC (ID, CATEGORY_ID, DESCRIPTION, IS_DELETED)
VALUES (topic_seq.nextval, category_seq.currval, '����������� ������', false);

INSERT INTO SUBTOPIC (ID, DESCRIPTION, IS_DELETED, TOPIC_ID)
VALUES (subtopic_seq.nextval, '������������������ ������', false, topic_seq.currval),
       (subtopic_seq.nextval, '���������� ������', false, topic_seq.currval);

INSERT INTO TOPIC (ID, CATEGORY_ID, DESCRIPTION, IS_DELETED)
VALUES (topic_seq.nextval, category_seq.currval, '���������� ���������� �� ������', false);

INSERT INTO SUBTOPIC (ID, DESCRIPTION, IS_DELETED, TOPIC_ID)
VALUES (subtopic_seq.nextval, '���������� �������� ����������', false, topic_seq.currval),
       (subtopic_seq.nextval, '���������� �������� ������ ���', false, topic_seq.currval);


INSERT INTO FEEDBACK (ID, AUTHOR_ID, CATEGORY_ID, CREATION_DATE, END_DATE, HEADER, IS_ACTUAL, PRIORITY_ID, STATUS_ID, SUBTOPIC_ID, TOPIC_ID, UPDATE_DATE)
VALUES (FEEDBACK_SEQ.nextval, 3, 1, CURRENT_TIME(), CURRENT_TIME(), '��� ������� � ������� ������', true, 'LOW', 'RESOLVED', 1, 1, CURRENT_TIME());

INSERT INTO FEEDBACK_FOLLOWER (ID, FEEDBACK_ID, FOLLOWER_TYPE, USER_ID)
VALUES (FOLLOWER_SEQ.nextval, FEEDBACK_SEQ.currval, 'ASSIGNEE', 2);

INSERT INTO FEEDBACK_MESSAGE  (ID, AUTHOR_ID, IS_DELETED, IS_UNREAD, MESSAGE_DATE, MESSAGE_TEXT, MESSAGE_TYPE, FEEDBACK_ID)
VALUES (FEEDBACK_MESSAGE_SEQ.nextval, 3, false, true, CURRENT_TIME(), '�� ���� �������� ������ � ������� ������', 'body', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 2, false, true, CURRENT_TIME(), '� �� ��������� ������������� ���������?', 'message', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 3, false, true, CURRENT_TIME(), '�������', 'message', FEEDBACK_SEQ.currval);


INSERT INTO FEEDBACK (ID, AUTHOR_ID, CATEGORY_ID, CREATION_DATE, END_DATE, HEADER, IS_ACTUAL, PRIORITY_ID, STATUS_ID, SUBTOPIC_ID, TOPIC_ID, UPDATE_DATE)
VALUES (FEEDBACK_SEQ.nextval, 3, 2, CURRENT_TIME(), CURRENT_TIME(), '��������� ���� �� ������', true, 'MEDIUM', 'CREATED', 5, 3, CURRENT_TIME());

INSERT INTO FEEDBACK_MESSAGE  (ID, AUTHOR_ID, IS_DELETED, IS_UNREAD, MESSAGE_DATE, MESSAGE_TEXT, MESSAGE_TYPE, FEEDBACK_ID)
VALUES (FEEDBACK_MESSAGE_SEQ.nextval, 3, false, true, CURRENT_TIME(), '�� ����� ��� ����� ������� ��� � ������???', 'body', FEEDBACK_SEQ.currval);


INSERT INTO FEEDBACK (ID, AUTHOR_ID, CATEGORY_ID, CREATION_DATE, END_DATE, HEADER, IS_ACTUAL, PRIORITY_ID, STATUS_ID, SUBTOPIC_ID, TOPIC_ID, UPDATE_DATE)
VALUES (FEEDBACK_SEQ.nextval, 4, 1, CURRENT_TIME(), CURRENT_TIME(), '����������� ������� �������', true, 'MEDIUM', 'IN_PROGRESS', 2, 1, CURRENT_TIME());

INSERT INTO FEEDBACK_FOLLOWER (ID, FEEDBACK_ID, FOLLOWER_TYPE, USER_ID)
VALUES (FOLLOWER_SEQ.nextval, FEEDBACK_SEQ.currval, 'ASSIGNEE', 1);

INSERT INTO FEEDBACK_MESSAGE  (ID, AUTHOR_ID, IS_DELETED, IS_UNREAD, MESSAGE_DATE, MESSAGE_TEXT, MESSAGE_TYPE, FEEDBACK_ID)
VALUES (FEEDBACK_MESSAGE_SEQ.nextval, 4, false, true, CURRENT_TIME(), '����� ���������� ������� ������
* �����
* �����', 'body', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 1, false, true, CURRENT_TIME(), '�� ��� ����� ���� ��� �����?', 'message', FEEDBACK_SEQ.currval),
       (FEEDBACK_MESSAGE_SEQ.nextval, 4, false, true, CURRENT_TIME(), '> �� ��� ����� ���� ��� �����?
����� ��� � �� �����, �� �������, ��������', 'message', FEEDBACK_SEQ.currval);