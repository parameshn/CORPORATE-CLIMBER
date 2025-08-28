-- Create tables
-- Create database
CREATE DATABASE corporate_climber;

-- Create user
CREATE USER corp_user WITH ENCRYPTED PASSWORD 'corp_password';
GRANT ALL PRIVILEGES ON DATABASE corporate_climber TO corp_user;

-- Connect to database
\c corporate_climber;

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- User career goals table
CREATE TABLE user_career_goals (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    goal VARCHAR(500) NOT NULL,
    PRIMARY KEY (user_id, goal)
);

-- Peers table (continued)
CREATE TABLE peers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    skill_level INTEGER DEFAULT 1 CHECK (skill_level >= 1 AND skill_level <= 10),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Managers table
CREATE TABLE managers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(255) NOT NULL,
    mentorship_rating DECIMAL(3,2) DEFAULT 0.0 CHECK (mentorship_rating >= 0.0 AND mentorship_rating <= 10.0),
    leadership_score DECIMAL(3,2) DEFAULT 0.0 CHECK (leadership_score >= 0.0 AND leadership_score <= 10.0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Projects table
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    domain VARCHAR(255) NOT NULL,
    priority VARCHAR(50) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    duration_months INTEGER NOT NULL CHECK (duration_months > 0),
    complexity_score INTEGER NOT NULL CHECK (complexity_score >= 1 AND complexity_score <= 10),
    status VARCHAR(50) NOT NULL DEFAULT 'PLANNING' CHECK (status IN ('PLANNING', 'ACTIVE', 'ON_HOLD', 'COMPLETED', 'CANCELLED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Project requirements table
CREATE TABLE project_requirements (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    skill_name VARCHAR(255) NOT NULL,
    required_level INTEGER NOT NULL CHECK (required_level >= 1 AND required_level <= 10),
    importance_weight DECIMAL(3,2) DEFAULT 1.0 CHECK (importance_weight >= 0.0 AND importance_weight <= 1.0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Project team assignments
CREATE TABLE project_peers (
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    peer_id BIGINT NOT NULL REFERENCES peers(id) ON DELETE CASCADE,
    role_in_project VARCHAR(255) NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (project_id, peer_id)
);

CREATE TABLE project_managers (
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    manager_id BIGINT NOT NULL REFERENCES managers(id) ON DELETE CASCADE,
    role_in_project VARCHAR(255) NOT NULL DEFAULT 'MANAGER',
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (project_id, manager_id)
);

-- User skills table
CREATE TABLE user_skills (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    skill_name VARCHAR(255) NOT NULL,
    current_level INTEGER NOT NULL CHECK (current_level >= 1 AND current_level <= 10),
    target_level INTEGER CHECK (target_level >= current_level AND target_level <= 10),
    last_assessed TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, skill_name)
);

-- Peer skills table
CREATE TABLE peer_skills (
    id BIGSERIAL PRIMARY KEY,
    peer_id BIGINT NOT NULL REFERENCES peers(id) ON DELETE CASCADE,
    skill_name VARCHAR(255) NOT NULL,
    skill_level INTEGER NOT NULL CHECK (skill_level >= 1 AND skill_level <= 10),
    verified_by VARCHAR(255),
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(peer_id, skill_name)
);

-- Conversation records table
CREATE TABLE conversation_records (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    peer_id BIGINT NOT NULL REFERENCES peers(id) ON DELETE CASCADE,
    conversation_text TEXT NOT NULL,
    platform VARCHAR(50) NOT NULL DEFAULT 'SLACK' CHECK (platform IN ('SLACK', 'TEAMS', 'EMAIL', 'MEETING', 'OTHER')),
    conversation_date TIMESTAMP NOT NULL,
    processed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Emotion analysis results
CREATE TABLE emotion_scores (
    id BIGSERIAL PRIMARY KEY,
    conversation_id BIGINT NOT NULL REFERENCES conversation_records(id) ON DELETE CASCADE,
    sentiment_score DECIMAL(3,2) NOT NULL CHECK (sentiment_score >= -1.0 AND sentiment_score <= 1.0),
    emotion_category VARCHAR(50) NOT NULL,
    confidence_score DECIMAL(3,2) NOT NULL CHECK (confidence_score >= 0.0 AND confidence_score <= 1.0),
    magnitude DECIMAL(3,2) NOT NULL CHECK (magnitude >= 0.0),
    analysis_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Peer interaction scores (aggregated)
CREATE TABLE peer_interactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    peer_id BIGINT NOT NULL REFERENCES peers(id) ON DELETE CASCADE,
    average_sentiment DECIMAL(3,2) NOT NULL CHECK (average_sentiment >= -1.0 AND average_sentiment <= 1.0),
    interaction_frequency INTEGER DEFAULT 0,
    synergy_score DECIMAL(3,2) NOT NULL CHECK (synergy_score >= 0.0 AND synergy_score <= 10.0),
    last_interaction TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, peer_id)
);

-- Monte Carlo simulation runs
CREATE TABLE simulation_runs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    number_of_trials INTEGER NOT NULL DEFAULT 10000,
    average_success_score DECIMAL(5,3) NOT NULL,
    success_probability DECIMAL(5,3) NOT NULL CHECK (success_probability >= 0.0 AND success_probability <= 1.0),
    expected_skill_gain DECIMAL(5,3) NOT NULL,
    peer_synergy_score DECIMAL(5,3) NOT NULL,
    goal_alignment_score DECIMAL(5,3) NOT NULL,
    confidence_interval_low DECIMAL(5,3) NOT NULL,
    confidence_interval_high DECIMAL(5,3) NOT NULL,
    simulated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Recommendations table
CREATE TABLE recommendations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    simulation_run_id BIGINT NOT NULL REFERENCES simulation_runs(id) ON DELETE CASCADE,
    recommendation_text TEXT NOT NULL,
    recommendation_type VARCHAR(50) NOT NULL CHECK (recommendation_type IN ('PRIMARY', 'ALTERNATIVE', 'CAUTION')),
    confidence DECIMAL(5,3) NOT NULL CHECK (confidence >= 0.0 AND confidence <= 1.0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- User project history (for learning from past selections)
CREATE TABLE user_project_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    selected_at TIMESTAMP NOT NULL,
    completion_rating INTEGER CHECK (completion_rating >= 1 AND completion_rating <= 10),
    skill_growth_actual DECIMAL(5,3),
    satisfaction_score INTEGER CHECK (satisfaction_score >= 1 AND satisfaction_score <= 10),
    retrospective_notes TEXT,
    completed_at TIMESTAMP
);

-- Indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_department ON users(department);
CREATE INDEX idx_projects_domain ON projects(domain);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_conversation_records_user_peer ON conversation_records(user_id, peer_id);
CREATE INDEX idx_conversation_records_date ON conversation_records(conversation_date);
CREATE INDEX idx_emotion_scores_conversation ON emotion_scores(conversation_id);
CREATE INDEX idx_simulation_runs_user ON simulation_runs(user_id);
CREATE INDEX idx_simulation_runs_project ON simulation_runs(project_id);
CREATE INDEX idx_simulation_runs_simulated_at ON simulation_runs(simulated_at);
CREATE INDEX idx_peer_interactions_user_peer ON peer_interactions(user_id, peer_id);
CREATE INDEX idx_user_skills_user ON user_skills(user_id);
CREATE INDEX idx_peer_skills_peer ON peer_skills(peer_id);

-- Grant permissions to the application user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO corp_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO corp_user;