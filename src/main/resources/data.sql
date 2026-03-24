-- =====================================================
-- 1. Добавление пользователей
-- =====================================================
INSERT INTO users (username, role) VALUES
('alinacozy', 'admin'),
('john_doe', 'user');

-- =====================================================
-- 2. Добавление ниток
-- =====================================================
INSERT INTO flosses (brand, color_number, color_name, color_group, red, green, blue) VALUES
('DMC', '321', 'Christmas Red', 'Red', 201, 35, 38),
('DMC', '310', 'Black', 'Black', 0, 0, 0),
('DMC', '5200', 'Snow White', 'White', 255, 255, 255),
('Anchor', '100', 'Red', 'Red', 200, 40, 45),
('Anchor', '403', 'Black', 'Black', 10, 10, 15),
('Madeira', '1001', 'Scarlet', 'Red', 210, 30, 35),
('Gamma', '111', 'Bright Red', 'Red', 215, 25, 30);

-- =====================================================
-- 3. Добавление проектов
-- =====================================================
-- Проекты для пользователя alinacozy (id=1)
INSERT INTO projects (project_name, user_id, description, start_date, status, pattern_src) VALUES
('Шотландская клетка', 1, 'Классический шотландский узор в красных тонах', CURRENT_TIMESTAMP, 'in_progress', '/patterns/scottish.pdf'),
('Гейзер', 1, 'Горный пейзаж с водопадом', CURRENT_TIMESTAMP, 'planning', '/patterns/geyser.pdf'),
('Розовый закат', 1, 'Нежный закат над морем', CURRENT_TIMESTAMP, 'completed', '/patterns/sunset.pdf');

-- Проекты для пользователя john_doe (id=2)
INSERT INTO projects (project_name, user_id, description, start_date, status, pattern_src) VALUES
('Лесная поляна', 2, 'Цветущая поляна в лесу', CURRENT_TIMESTAMP, 'in_progress', '/patterns/forest.pdf'),
('Морской бриз', 2, 'Морской пейзаж с парусником', CURRENT_TIMESTAMP, 'planning', '/patterns/sea.pdf');

-- =====================================================
-- 4. Добавление инвентаря (инвентарь для пользователей)
-- =====================================================
-- Инвентарь для alinacozy (user_id=1)
INSERT INTO inventory (floss_id, user_id, quantity) VALUES
((SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '321'), 1, 5),
((SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '310'), 1, 3),
((SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '5200'), 1, 10),
((SELECT id FROM flosses WHERE brand = 'Anchor' AND color_number = '100'), 1, 2),
((SELECT id FROM flosses WHERE brand = 'Gamma' AND color_number = '111'), 1, 4);

-- Инвентарь для john_doe (user_id=2)
INSERT INTO inventory (floss_id, user_id, quantity) VALUES
((SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '321'), 2, 3),
((SELECT id FROM flosses WHERE brand = 'Anchor' AND color_number = '403'), 2, 7),
((SELECT id FROM flosses WHERE brand = 'Madeira' AND color_number = '1001'), 2, 2),
((SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '310'), 2, 5),
((SELECT id FROM flosses WHERE brand = 'Anchor' AND color_number = '100'), 2, 1);

-- =====================================================
-- 5. Добавление требуемых ниток для проектов
-- =====================================================
-- Требуемые нитки для проекта "Шотландская клетка" (id=1)
INSERT INTO required_flosses (project_id, floss_id, quantity, custom_red, custom_green, custom_blue, custom_color_name) VALUES
(1, (SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '321'), 3, NULL, NULL, NULL, NULL),
(1, (SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '310'), 2, NULL, NULL, NULL, NULL),
(1, (SELECT id FROM flosses WHERE brand = 'Anchor' AND color_number = '100'), 1, NULL, NULL, NULL, NULL),
(1, NULL, 2, 180, 100, 50, 'Коричневый');

-- Требуемые нитки для проекта "Гейзер" (id=2)
INSERT INTO required_flosses (project_id, floss_id, quantity, custom_red, custom_green, custom_blue, custom_color_name) VALUES
(2, (SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '5200'), 4, NULL, NULL, NULL, NULL),
(2, (SELECT id FROM flosses WHERE brand = 'Madeira' AND color_number = '1001'), 2, NULL, NULL, NULL, NULL),
(2, (SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '321'), 1, NULL, NULL, NULL, NULL);

-- Требуемые нитки для проекта "Розовый закат" (id=3)
INSERT INTO required_flosses (project_id, floss_id, quantity, custom_red, custom_green, custom_blue, custom_color_name) VALUES
(3, (SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '321'), 5, NULL, NULL, NULL, NULL),
(3, (SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '5200'), 3, NULL, NULL, NULL, NULL),
(3, NULL, 2, 255, 200, 200, 'Нежно-розовый'),
(3, NULL, 2, 255, 150, 150, 'Средний розовый');

-- Требуемые нитки для проекта "Лесная поляна" (id=4)
INSERT INTO required_flosses (project_id, floss_id, quantity, custom_red, custom_green, custom_blue, custom_color_name) VALUES
(4, (SELECT id FROM flosses WHERE brand = 'Gamma' AND color_number = '111'), 3, NULL, NULL, NULL, NULL),
(4, (SELECT id FROM flosses WHERE brand = 'Anchor' AND color_number = '403'), 2, NULL, NULL, NULL, NULL),
(4, NULL, 4, 100, 150, 80, 'Лесной зеленый'),
(4, NULL, 2, 210, 180, 100, 'Песочный');

-- Требуемые нитки для проекта "Морской бриз" (id=5)
INSERT INTO required_flosses (project_id, floss_id, quantity, custom_red, custom_green, custom_blue, custom_color_name) VALUES
(5, (SELECT id FROM flosses WHERE brand = 'Anchor' AND color_number = '100'), 3, NULL, NULL, NULL, NULL),
(5, (SELECT id FROM flosses WHERE brand = 'DMC' AND color_number = '5200'), 5, NULL, NULL, NULL, NULL),
(5, NULL, 3, 50, 100, 180, 'Морская волна'),
(5, NULL, 2, 150, 200, 210, 'Голубой');
