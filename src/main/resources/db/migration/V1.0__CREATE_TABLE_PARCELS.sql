CREATE SCHEMA IF NOT EXISTS package_load;

-- Установка текущей схемы на package_load
SET search_path TO package_load;

-- Создание таблицы parcels, если она не существует
CREATE TABLE IF NOT EXISTS package_load.parcels (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    form VARCHAR(500) NOT NULL,
    symbol VARCHAR(1) NOT NULL
);

-- Вставка начальных данных в таблицу parcels
INSERT INTO package_load.parcels (name, form, symbol) VALUES
('Штанга', E'@     @\n@@@@@@@\n@     @', '@'),
('Табурет', E'№№№№\n№   №\n№   №', '№'),
('Кружка', E'######\n#    ##\n#    ##\n###### ', '#'),
('Стол', E'$$$$$$$\n   $   \n   $   \n   $   ', '$'),
('Лодка', E'%      %\n%      %\n%%%%%%%%', '%'),
('Стул', E'^    \n^    \n^^^^^\n^   ^\n^   ^', '^'),
('Часы', E'  *  \n * * \n*   *\n * * \n  *  ', '*'),
('Один', E'1', '1'),
('Два', E'22', '2'),
('Три', E'333', '3'),
('Четыре', E'4444', '4'),
('Пять', E'55555', '5'),
('Шесть', E'666\n666', '6'),
('Семь', E'777\n7777', '7'),
('Восемь', E'8888\n8888', '8'),
('Девять', E'999\n999\n999', '9');