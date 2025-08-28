-- Optional: Create and use the database
CREATE DATABASE IF NOT EXISTS corporate_climber;
USE corporate_climber;

-- Drop tables in reverse order of dependency
DROP TABLE IF EXISTS recommendations;
DROP TABLE IF EXISTS simulation_runs;
DROP TABLE IF EXISTS emotion_scores;
DROP TABLE IF EXISTS conversation_records;
DROP TABLE IF EXISTS peer_skills;
DROP TABLE IF EXISTS project_requirements;
DROP TABLE IF EXISTS project_peers;
DROP TABLE IF EXISTS user_skills;
DROP TABLE IF EXISTS user_career_goals;
DROP TABLE IF EXISTS peers;
DROP TABLE IF EXISTS managers;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS users;

-- Users
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Career Goals
CREATE TABLE user_career_goals (
    user_id BIGINT NOT NULL,
    goal VARCHAR(500) NOT NULL,
    PRIMARY KEY (user_id, goal),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Peers
CREATE TABLE peers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    skill_level INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Peer Skills
CREATE TABLE peer_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    peer_id BIGINT NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    skill_level INT NOT NULL,
    verified_by VARCHAR(255),
    last_updated TIMESTAMP NOT NULL,
    FOREIGN KEY (peer_id) REFERENCES peers(id) ON DELETE CASCADE
);

-- Managers
CREATE TABLE managers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(255) NOT NULL,
    mentorship_rating DOUBLE NOT NULL,
    leadership_score DOUBLE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Projects
CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    domain VARCHAR(255) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PLANNING',
    duration_months INT NOT NULL,
    complexity_score DOUBLE NOT NULL,
    manager_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (manager_id) REFERENCES managers(id)
);

-- Project Requirements
CREATE TABLE project_requirements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    required_level INT NOT NULL,
    importance_weight DOUBLE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Project Peers
CREATE TABLE project_peers (
    project_id BIGINT NOT NULL,
    peer_id BIGINT NOT NULL,
    PRIMARY KEY (project_id, peer_id),
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (peer_id) REFERENCES peers(id) ON DELETE CASCADE
);

-- User Skills
CREATE TABLE user_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    current_level INT NOT NULL,
    target_level INT,
    last_assessed TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE (user_id, skill_name)
);

-- Conversation Records
CREATE TABLE conversation_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    peer_id BIGINT NOT NULL,
    conversation_text TEXT NOT NULL,
    platform VARCHAR(50) NOT NULL,
    conversation_date TIMESTAMP NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (peer_id) REFERENCES peers(id) ON DELETE CASCADE
);

-- Emotion Scores
CREATE TABLE emotion_scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id BIGINT NOT NULL,
    sentiment_score DOUBLE NOT NULL,
    emotion_category VARCHAR(50) NOT NULL,
    confidence_score DOUBLE NOT NULL,
    magnitude DOUBLE NOT NULL,
    analysis_timestamp TIMESTAMP NOT NULL,
    FOREIGN KEY (conversation_id) REFERENCES conversation_records(id) ON DELETE CASCADE
);

-- Simulation Runs
CREATE TABLE simulation_runs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    number_of_trials INT NOT NULL,
    average_success_score DOUBLE NOT NULL,
    success_probability DOUBLE NOT NULL,
    expected_skill_gain DOUBLE NOT NULL,
    peer_synergy_score DOUBLE NOT NULL,
    goal_alignment_score DOUBLE NOT NULL,
    confidence_interval_low DOUBLE NOT NULL,
    confidence_interval_high DOUBLE NOT NULL,
    simulated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- Recommendations
CREATE TABLE recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    simulation_run_id BIGINT NOT NULL,
    recommendation_text TEXT NOT NULL,
    recommendation_type VARCHAR(50) NOT NULL,
    confidence DOUBLE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (simulation_run_id) REFERENCES simulation_runs(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_department ON users(department);
CREATE INDEX idx_projects_domain ON projects(domain);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_conversations_user_peer ON conversation_records(user_id, peer_id);
CREATE INDEX idx_conversations_date ON conversation_records(conversation_date);
CREATE INDEX idx_emotion_scores_sentiment ON emotion_scores(sentiment_score);
CREATE INDEX idx_simulations_user ON simulation_runs(user_id);
CREATE INDEX idx_simulations_project ON simulation_runs(project_id);
CREATE INDEX idx_user_skills_user ON user_skills(user_id);
CREATE INDEX idx_project_reqs_project ON project_requirements(project_id);
CREATE INDEX idx_peer_skills_peer ON peer_skills(peer_id);
CREATE INDEX idx_peer_skills_skill_name ON peer_skills(skill_name);
