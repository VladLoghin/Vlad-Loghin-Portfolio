-- ============ SCHEMA ============

CREATE TABLE IF NOT EXISTS education (
    id BIGSERIAL PRIMARY KEY,
    institution_name TEXT NOT NULL,
    degree TEXT,
    active BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS hobbies (
    id BIGSERIAL PRIMARY KEY,
    hobby_name TEXT NOT NULL,
    description TEXT,
    tags TEXT DEFAULT '[]',
    active BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS projects (
    id BIGSERIAL PRIMARY KEY,
    project_name TEXT NOT NULL,
    tag TEXT,
    description TEXT,
    skills TEXT DEFAULT '[]',
    github_url TEXT,
    active BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS skills (
    id SERIAL PRIMARY KEY,
    skill_name TEXT NOT NULL,
    subskills TEXT DEFAULT '[]',
    active BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGSERIAL PRIMARY KEY,
    reviewer_name TEXT NOT NULL,
    content TEXT,
    rating TEXT,
    approved BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS sections (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL UNIQUE,
    type TEXT NOT NULL,
    body TEXT,
    items TEXT DEFAULT '[]'
);

CREATE TABLE IF NOT EXISTS cv_documents (
    id BIGSERIAL PRIMARY KEY,
    language TEXT NOT NULL UNIQUE,
    filename TEXT,
    data BYTEA,
    content_type TEXT,
    uploaded_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS profile_images (
    id BIGSERIAL PRIMARY KEY,
    filename TEXT,
    data BYTEA,
    content_type TEXT,
    uploaded_at TIMESTAMP DEFAULT NOW()
);
