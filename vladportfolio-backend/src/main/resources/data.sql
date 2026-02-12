-- ============ EDUCATION ============
INSERT INTO education (institution_name, degree, active)
SELECT 'Champlain College St-Lambert', 'Computer Science', true
WHERE NOT EXISTS (SELECT 1 FROM education WHERE institution_name = 'Champlain College St-Lambert');

INSERT INTO education (institution_name, degree, active)
SELECT 'Self-Study', 'Spring Boot + React + Docker', true
WHERE NOT EXISTS (SELECT 1 FROM education WHERE institution_name = 'Self-Study');

-- ============ HOBBIES ============
INSERT INTO hobbies (hobby_name, description, tags, active)
SELECT 'Music', 'I love to play and learn songs that I love musically.', '["Guitar"]', true
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Music');

INSERT INTO hobbies (hobby_name, description, tags, active)
SELECT 'Game Design', 'Puzzle systems, time-based mechanics, and narrative worlds that reward observation and experimentation.', '["Systems","Level design","Lore","Prototyping"]', true
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Game Design');

INSERT INTO hobbies (hobby_name, description, tags, active)
SELECT 'Tabletop & Strategy', 'Competitive deckbuilding and strategy games—good practice for planning, tradeoffs, and adaptation.', '["Magic: The Gathering","Warhammer","Collecting"]', true
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Tabletop & Strategy');

INSERT INTO hobbies (hobby_name, description, tags, active)
SELECT 'Reading', 'I enjoy reading from time to time to unwind and escape for a little while. Some of my favourite series to read are the A Song of Ice and Fire books, the new Ultimate Marvel universe comics, the DC Absolute universe comics, and my favourite philosophical book is Meditations by Marcus Aurelius.', '["Comics","Fantasy","Philosophy"]', true
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Reading');

INSERT INTO hobbies (hobby_name, description, tags, active)
SELECT 'Gaming', 'One of the hobbies that has stuck with me over the years, games are a form of higher art taking into account, music, visuals, storytelling and many more things. Some of my favourite games are Crusader Kings 3, Total War Warhammer 3 and Devil May Cry 5. My favourite game character is Dante. He''s so cool!', '["RPGs","Strategy","Puzzle","Adventure","Hack and Slash"]', true
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Gaming');

-- ============ PROJECTS ============
INSERT INTO projects (project_name, tag, description, skills, active)
SELECT 'VladTech', 'Full-stack', 'Renovation business platform with role-based access, reviews/portfolio modules, and production-ready deployment.', '["Spring Boot","React","MongoDB","Auth0","Docker"]', true
WHERE NOT EXISTS (SELECT 1 FROM projects WHERE project_name = 'VladTech');

INSERT INTO projects (project_name, tag, description, skills, active)
SELECT 'Unnamed Game Project', 'In progress', 'I am currently working and learning about game design and development for a 2D platformer puzzle game with plants. For this project I''ve called upon the help of a master''s student in game design and a graduate from a level design bootcamp', '["Unity 2d","C#","Game Design","Puzzle Mechanics"]', true
WHERE NOT EXISTS (SELECT 1 FROM projects WHERE project_name = 'Unnamed Game Project');

INSERT INTO projects (project_name, tag, description, skills, active)
SELECT 'PetClinic', 'Full-stack', 'Worked on the PetClinic Billing team, was Scrum Master and developer, worked on emailing and making requests not be static.', '["Spring","WebFlux","Docker Compose","CI"]', true
WHERE NOT EXISTS (SELECT 1 FROM projects WHERE project_name = 'PetClinic');

-- ============ REVIEWS ============
INSERT INTO reviews (reviewer_name, content, rating, approved)
SELECT 'Anonymous Teammate', 'Vlad consistently took ownership of the hard parts and still made time to help others unblock.', 'FIVE', true
WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE reviewer_name = 'Anonymous Teammate');

INSERT INTO reviews (reviewer_name, content, rating, approved)
SELECT 'Mentor', 'Strong engineering instincts. When something breaks, he traces it to the root cause instead of patching.', 'FOUR', true
WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE reviewer_name = 'Mentor');

INSERT INTO reviews (reviewer_name, content, rating, approved)
SELECT 'Client', 'Clear communication, fast turnaround, and the end result felt polished—not a rough prototype.', 'FIVE', true
WHERE NOT EXISTS (SELECT 1 FROM reviews WHERE reviewer_name = 'Client');

-- ============ SECTIONS ============
INSERT INTO sections (title, type, body, items)
SELECT 'About', 'PARAGRAPH', 'I''m Vlad Loghin — a software developer who builds clean, reliable web apps with thoughtful UX. I like shipping features fast, then tightening the engineering until it''s solid.', '[]'
WHERE NOT EXISTS (SELECT 1 FROM sections WHERE title = 'About');

INSERT INTO sections (title, type, body, items)
SELECT 'What I do', 'BULLETS', null, '["Java / Spring Boot APIs","Svelte & React frontends","Docker & CI pipelines"]'
WHERE NOT EXISTS (SELECT 1 FROM sections WHERE title = 'What I do');

INSERT INTO sections (title, type, body, items)
SELECT 'What I care about', 'TAGS', null, '["Good Software","Clean Code","New Challenges","Continuous Learning","Creativity"]'
WHERE NOT EXISTS (SELECT 1 FROM sections WHERE title = 'What I care about');

-- ============ SKILLS ============
INSERT INTO skills (skill_name, subskills, active)
SELECT 'Backend', '["Java","Spring Boot","Spring WebFlux","REST APIs","Docker","CI/CD"]', true
WHERE NOT EXISTS (SELECT 1 FROM skills WHERE skill_name = 'Backend');

INSERT INTO skills (skill_name, subskills, active)
SELECT 'Frontend', '["Svelte","React","TypeScript","HTML/CSS","Responsive Design"]', true
WHERE NOT EXISTS (SELECT 1 FROM skills WHERE skill_name = 'Frontend');

INSERT INTO skills (skill_name, subskills, active)
SELECT 'Databases & Tools', '["MongoDB","MySQL","Git","Auth0"]', true
WHERE NOT EXISTS (SELECT 1 FROM skills WHERE skill_name = 'Databases & Tools');

INSERT INTO skills (skill_name, subskills, active)
SELECT 'Best Practices', '["Clean Code","Solid Principles","Testing","Agile","Code Review"]', true
WHERE NOT EXISTS (SELECT 1 FROM skills WHERE skill_name = 'Best Practices');
