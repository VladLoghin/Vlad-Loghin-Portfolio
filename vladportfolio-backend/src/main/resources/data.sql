-- ============ EDUCATION ============
INSERT INTO education (institution_name, degree)
SELECT 'Champlain College St-Lambert', 'Computer Science'
WHERE NOT EXISTS (SELECT 1 FROM education WHERE institution_name = 'Champlain College St-Lambert');

INSERT INTO education (institution_name, degree)
SELECT 'Self-Study', 'Spring Boot + React + Docker'
WHERE NOT EXISTS (SELECT 1 FROM education WHERE institution_name = 'Self-Study');

-- ============ HOBBIES ============
INSERT INTO hobbies (hobby_name, description)
SELECT 'Music', 'I love to play and learn songs that I love musically.'
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Music');

INSERT INTO hobbies (hobby_name, description)
SELECT 'Game Design', 'Puzzle systems, time-based mechanics, and narrative worlds that reward observation and experimentation.'
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Game Design');

INSERT INTO hobbies (hobby_name, description)
SELECT 'Tabletop & Strategy', 'Competitive deckbuilding and strategy games—good practice for planning, tradeoffs, and adaptation.'
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Tabletop & Strategy');

INSERT INTO hobbies (hobby_name, description)
SELECT 'Reading', 'I enjoy reading from time to time to unwind and escape for a little while. Some of my favourite series to read are the A Song of Ice and Fire books, the new Ultimate Marvel universe comics, the DC Absolute universe comics, and my favourite philosophical book is Meditations by Marcus Aurelius.'
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Reading');

INSERT INTO hobbies (hobby_name, description)
SELECT 'Gaming', 'One of the hobbies that has stuck with me over the years, games are a form of higher art taking into account, music, visuals, storytelling and many more things. Some of my favourite games are Crusader Kings 3, Total War Warhammer 3 and Devil May Cry 5. My favourite game character is Dante. He''s so cool!'
WHERE NOT EXISTS (SELECT 1 FROM hobbies WHERE hobby_name = 'Gaming');

-- Hobby tags
INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Guitar' FROM hobbies WHERE hobby_name = 'Music'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Music' AND ht.tags = 'Guitar');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Systems' FROM hobbies WHERE hobby_name = 'Game Design'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Game Design' AND ht.tags = 'Systems');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Level design' FROM hobbies WHERE hobby_name = 'Game Design'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Game Design' AND ht.tags = 'Level design');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Lore' FROM hobbies WHERE hobby_name = 'Game Design'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Game Design' AND ht.tags = 'Lore');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Prototyping' FROM hobbies WHERE hobby_name = 'Game Design'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Game Design' AND ht.tags = 'Prototyping');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Magic: The Gathering' FROM hobbies WHERE hobby_name = 'Tabletop & Strategy'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Tabletop & Strategy' AND ht.tags = 'Magic: The Gathering');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Warhammer' FROM hobbies WHERE hobby_name = 'Tabletop & Strategy'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Tabletop & Strategy' AND ht.tags = 'Warhammer');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Collecting' FROM hobbies WHERE hobby_name = 'Tabletop & Strategy'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Tabletop & Strategy' AND ht.tags = 'Collecting');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Comics' FROM hobbies WHERE hobby_name = 'Reading'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Reading' AND ht.tags = 'Comics');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Fantasy' FROM hobbies WHERE hobby_name = 'Reading'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Reading' AND ht.tags = 'Fantasy');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Philosophy' FROM hobbies WHERE hobby_name = 'Reading'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Reading' AND ht.tags = 'Philosophy');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'RPGs' FROM hobbies WHERE hobby_name = 'Gaming'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Gaming' AND ht.tags = 'RPGs');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Strategy' FROM hobbies WHERE hobby_name = 'Gaming'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Gaming' AND ht.tags = 'Strategy');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Puzzle' FROM hobbies WHERE hobby_name = 'Gaming'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Gaming' AND ht.tags = 'Puzzle');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Adventure' FROM hobbies WHERE hobby_name = 'Gaming'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Gaming' AND ht.tags = 'Adventure');

INSERT INTO hobby_tags (hobby_id, tags)
SELECT id, 'Hack and Slash' FROM hobbies WHERE hobby_name = 'Gaming'
AND NOT EXISTS (SELECT 1 FROM hobby_tags ht JOIN hobbies h ON ht.hobby_id = h.id WHERE h.hobby_name = 'Gaming' AND ht.tags = 'Hack and Slash');

-- ============ PROJECTS ============
INSERT INTO projects (project_name, tag, description)
SELECT 'VladTech', 'Full-stack', 'Renovation business platform with role-based access, reviews/portfolio modules, and production-ready deployment.'
WHERE NOT EXISTS (SELECT 1 FROM projects WHERE project_name = 'VladTech');

INSERT INTO projects (project_name, tag, description)
SELECT 'Unnamed Game Project', 'In progress', 'I am currently working and learning about game design and development for a 2D platformer puzzle game with plants. For this project I''ve called upon the help of a master''s student in game design and a graduate from a level design bootcamp'
WHERE NOT EXISTS (SELECT 1 FROM projects WHERE project_name = 'Unnamed Game Project');

INSERT INTO projects (project_name, tag, description)
SELECT 'PetClinic', 'Full-stack', 'Worked on the PetClinic Billing team, was Scrum Master and developer, worked on emailing and making requests not be static.'
WHERE NOT EXISTS (SELECT 1 FROM projects WHERE project_name = 'PetClinic');

-- Project skills
INSERT INTO project_skills (project_id, skills)
SELECT id, 'Spring Boot' FROM projects WHERE project_name = 'VladTech'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'VladTech' AND ps.skills = 'Spring Boot');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'React' FROM projects WHERE project_name = 'VladTech'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'VladTech' AND ps.skills = 'React');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'MongoDB' FROM projects WHERE project_name = 'VladTech'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'VladTech' AND ps.skills = 'MongoDB');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'Auth0' FROM projects WHERE project_name = 'VladTech'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'VladTech' AND ps.skills = 'Auth0');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'Docker' FROM projects WHERE project_name = 'VladTech'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'VladTech' AND ps.skills = 'Docker');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'Unity 2d' FROM projects WHERE project_name = 'Unnamed Game Project'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'Unnamed Game Project' AND ps.skills = 'Unity 2d');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'C#' FROM projects WHERE project_name = 'Unnamed Game Project'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'Unnamed Game Project' AND ps.skills = 'C#');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'Game Design' FROM projects WHERE project_name = 'Unnamed Game Project'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'Unnamed Game Project' AND ps.skills = 'Game Design');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'Puzzle Mechanics' FROM projects WHERE project_name = 'Unnamed Game Project'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'Unnamed Game Project' AND ps.skills = 'Puzzle Mechanics');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'Spring' FROM projects WHERE project_name = 'PetClinic'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'PetClinic' AND ps.skills = 'Spring');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'WebFlux' FROM projects WHERE project_name = 'PetClinic'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'PetClinic' AND ps.skills = 'WebFlux');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'Docker Compose' FROM projects WHERE project_name = 'PetClinic'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'PetClinic' AND ps.skills = 'Docker Compose');

INSERT INTO project_skills (project_id, skills)
SELECT id, 'CI' FROM projects WHERE project_name = 'PetClinic'
AND NOT EXISTS (SELECT 1 FROM project_skills ps JOIN projects p ON ps.project_id = p.id WHERE p.project_name = 'PetClinic' AND ps.skills = 'CI');

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
INSERT INTO sections (title, type, body)
SELECT 'About', 'PARAGRAPH', 'I''m Vlad Loghin — a software developer who builds clean, reliable web apps with thoughtful UX. I like shipping features fast, then tightening the engineering until it''s solid.'
WHERE NOT EXISTS (SELECT 1 FROM sections WHERE title = 'About');

INSERT INTO sections (title, type, body)
SELECT 'What I do', 'BULLETS', null
WHERE NOT EXISTS (SELECT 1 FROM sections WHERE title = 'What I do');

INSERT INTO sections (title, type, body)
SELECT 'What I care about', 'TAGS', null
WHERE NOT EXISTS (SELECT 1 FROM sections WHERE title = 'What I care about');

-- Section items
INSERT INTO section_items (section_id, item_order, item)
SELECT id, 0, 'Java / Spring Boot APIs' FROM sections WHERE title = 'What I do'
AND NOT EXISTS (SELECT 1 FROM section_items si JOIN sections s ON si.section_id = s.id WHERE s.title = 'What I do' AND si.item = 'Java / Spring Boot APIs');

INSERT INTO section_items (section_id, item_order, item)
SELECT id, 1, 'Svelte & React frontends' FROM sections WHERE title = 'What I do'
AND NOT EXISTS (SELECT 1 FROM section_items si JOIN sections s ON si.section_id = s.id WHERE s.title = 'What I do' AND si.item = 'Svelte & React frontends');

INSERT INTO section_items (section_id, item_order, item)
SELECT id, 2, 'Docker & CI pipelines' FROM sections WHERE title = 'What I do'
AND NOT EXISTS (SELECT 1 FROM section_items si JOIN sections s ON si.section_id = s.id WHERE s.title = 'What I do' AND si.item = 'Docker & CI pipelines');

INSERT INTO section_items (section_id, item_order, item)
SELECT id, 0, 'Good Software' FROM sections WHERE title = 'What I care about'
AND NOT EXISTS (SELECT 1 FROM section_items si JOIN sections s ON si.section_id = s.id WHERE s.title = 'What I care about' AND si.item = 'Good Software');

INSERT INTO section_items (section_id, item_order, item)
SELECT id, 1, 'Clean Code' FROM sections WHERE title = 'What I care about'
AND NOT EXISTS (SELECT 1 FROM section_items si JOIN sections s ON si.section_id = s.id WHERE s.title = 'What I care about' AND si.item = 'Clean Code');

INSERT INTO section_items (section_id, item_order, item)
SELECT id, 2, 'New Challenges' FROM sections WHERE title = 'What I care about'
AND NOT EXISTS (SELECT 1 FROM section_items si JOIN sections s ON si.section_id = s.id WHERE s.title = 'What I care about' AND si.item = 'New Challenges');

INSERT INTO section_items (section_id, item_order, item)
SELECT id, 3, 'Continuous Learning' FROM sections WHERE title = 'What I care about'
AND NOT EXISTS (SELECT 1 FROM section_items si JOIN sections s ON si.section_id = s.id WHERE s.title = 'What I care about' AND si.item = 'Continuous Learning');

INSERT INTO section_items (section_id, item_order, item)
SELECT id, 4, 'Creativity' FROM sections WHERE title = 'What I care about'
AND NOT EXISTS (SELECT 1 FROM section_items si JOIN sections s ON si.section_id = s.id WHERE s.title = 'What I care about' AND si.item = 'Creativity');

-- ============ SKILLS ============
INSERT INTO skills (skill_name)
SELECT 'Backend' WHERE NOT EXISTS (SELECT 1 FROM skills WHERE skill_name = 'Backend');

INSERT INTO skills (skill_name)
SELECT 'Frontend' WHERE NOT EXISTS (SELECT 1 FROM skills WHERE skill_name = 'Frontend');

INSERT INTO skills (skill_name)
SELECT 'Databases & Tools' WHERE NOT EXISTS (SELECT 1 FROM skills WHERE skill_name = 'Databases & Tools');

INSERT INTO skills (skill_name)
SELECT 'Best Practices' WHERE NOT EXISTS (SELECT 1 FROM skills WHERE skill_name = 'Best Practices');

-- Backend subskills
INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 0, 'Java' FROM skills WHERE skill_name = 'Backend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Backend' AND ss.subskill = 'Java');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 1, 'Spring Boot' FROM skills WHERE skill_name = 'Backend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Backend' AND ss.subskill = 'Spring Boot');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 2, 'Spring WebFlux' FROM skills WHERE skill_name = 'Backend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Backend' AND ss.subskill = 'Spring WebFlux');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 3, 'REST APIs' FROM skills WHERE skill_name = 'Backend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Backend' AND ss.subskill = 'REST APIs');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 4, 'Docker' FROM skills WHERE skill_name = 'Backend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Backend' AND ss.subskill = 'Docker');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 5, 'CI/CD' FROM skills WHERE skill_name = 'Backend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Backend' AND ss.subskill = 'CI/CD');

-- Frontend subskills
INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 0, 'Svelte' FROM skills WHERE skill_name = 'Frontend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Frontend' AND ss.subskill = 'Svelte');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 1, 'React' FROM skills WHERE skill_name = 'Frontend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Frontend' AND ss.subskill = 'React');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 2, 'TypeScript' FROM skills WHERE skill_name = 'Frontend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Frontend' AND ss.subskill = 'TypeScript');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 3, 'HTML/CSS' FROM skills WHERE skill_name = 'Frontend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Frontend' AND ss.subskill = 'HTML/CSS');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 4, 'Responsive Design' FROM skills WHERE skill_name = 'Frontend'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Frontend' AND ss.subskill = 'Responsive Design');

-- Databases & Tools subskills
INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 0, 'MongoDB' FROM skills WHERE skill_name = 'Databases & Tools'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Databases & Tools' AND ss.subskill = 'MongoDB');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 1, 'MySQL' FROM skills WHERE skill_name = 'Databases & Tools'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Databases & Tools' AND ss.subskill = 'MySQL');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 2, 'Git' FROM skills WHERE skill_name = 'Databases & Tools'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Databases & Tools' AND ss.subskill = 'Git');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 3, 'Auth0' FROM skills WHERE skill_name = 'Databases & Tools'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Databases & Tools' AND ss.subskill = 'Auth0');

-- Best Practices subskills
INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 0, 'Clean Code' FROM skills WHERE skill_name = 'Best Practices'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Best Practices' AND ss.subskill = 'Clean Code');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 1, 'Solid Principles' FROM skills WHERE skill_name = 'Best Practices'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Best Practices' AND ss.subskill = 'Solid Principles');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 2, 'Testing' FROM skills WHERE skill_name = 'Best Practices'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Best Practices' AND ss.subskill = 'Testing');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 3, 'Agile' FROM skills WHERE skill_name = 'Best Practices'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Best Practices' AND ss.subskill = 'Agile');

INSERT INTO skill_subskills (skill_id, subskill_order, subskill)
SELECT id, 4, 'Code Review' FROM skills WHERE skill_name = 'Best Practices'
AND NOT EXISTS (SELECT 1 FROM skill_subskills ss JOIN skills s ON ss.skill_id = s.id WHERE s.skill_name = 'Best Practices' AND ss.subskill = 'Code Review');
