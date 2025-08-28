-- Users
INSERT INTO users (email, first_name, last_name, department, role, created_at, updated_at) VALUES
('john.doe@corp.com', 'John', 'Doe', 'Engineering', 'Senior Developer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('jane.smith@corp.com', 'Jane', 'Smith', 'Data Science', 'ML Engineer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- User career goals
INSERT INTO user_career_goals (user_id, goal) VALUES
(1, 'Become a technical architect'),
(1, 'Lead cloud migration projects'),
(2, 'Develop expertise in deep learning');

-- Peers
INSERT INTO peers (name, email, department, role, skill_level, created_at, updated_at) VALUES
('Alice Chen', 'alice.chen@corp.com', 'Engineering', 'Frontend Developer', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob Johnson', 'bob.johnson@corp.com', 'Data Science', 'Data Engineer', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Peer skills
INSERT INTO peer_skills (peer_id, skill_name, skill_level, verified_by, last_updated) VALUES
(1, 'React', 8, 'Sarah Williams', CURRENT_TIMESTAMP),
(1, 'UI/UX Design', 7, 'Mike Thompson', CURRENT_TIMESTAMP),
(2, 'Data Engineering', 9, 'Sarah Williams', CURRENT_TIMESTAMP),
(2, 'Python', 8, 'Mike Thompson', CURRENT_TIMESTAMP);

-- Managers
INSERT INTO managers (name, email, department, mentorship_rating, leadership_score, created_at, updated_at) VALUES
('Sarah Williams', 'sarah.w@corp.com', 'Engineering', 8.5, 9.2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mike Thompson', 'mike.t@corp.com', 'Data Science', 9.0, 8.7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Projects
INSERT INTO projects (name, description, domain, priority, status, duration_months, complexity_score, manager_id, created_at, updated_at) VALUES
('AI Recommendation Engine', 'Build next-gen recommendation system', 'AI', 'HIGH', 'PLANNING', 6, 8.5, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Data Lake Migration', 'Migrate legacy data to cloud data lake', 'Cloud', 'MEDIUM', 'ACTIVE', 8, 7.2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Project requirements
INSERT INTO project_requirements (project_id, skill_name, required_level, importance_weight, created_at) VALUES
(1, 'Machine Learning', 8, 1.0, CURRENT_TIMESTAMP),
(1, 'Python', 9, 0.9, CURRENT_TIMESTAMP),
(2, 'AWS', 8, 1.0, CURRENT_TIMESTAMP),
(2, 'Data Modeling', 7, 0.8, CURRENT_TIMESTAMP);

-- Project peers
INSERT INTO project_peers (project_id, peer_id) VALUES
(1, 1),
(1, 2),
(2, 2);

-- User skills
INSERT INTO user_skills (user_id, skill_name, current_level, target_level, last_assessed, created_at, updated_at) VALUES
(1, 'Java', 9, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 'Cloud Architecture', 7, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Python', 9, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Machine Learning', 8, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Conversation records
INSERT INTO conversation_records (user_id, peer_id, conversation_text, platform, conversation_date, created_at, processed) VALUES
(1, 1, 'Discussed API integration challenges', 'SLACK', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY), CURRENT_TIMESTAMP, FALSE),
(1, 2, 'Reviewed data pipeline design', 'MEETING', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY), CURRENT_TIMESTAMP, FALSE);

-- Emotion scores
INSERT INTO emotion_scores (conversation_id, sentiment_score, emotion_category, confidence_score, magnitude, analysis_timestamp) VALUES
(1, 0.8, 'SATISFACTION', 0.92, 0.9, CURRENT_TIMESTAMP),
(2, 0.6, 'NEUTRAL', 0.85, 0.7, CURRENT_TIMESTAMP);

-- Simulation runs
INSERT INTO simulation_runs (user_id, project_id, number_of_trials, average_success_score, success_probability, expected_skill_gain, peer_synergy_score, goal_alignment_score, confidence_interval_low, confidence_interval_high, simulated_at) VALUES
(1, 1, 10000, 0.85, 0.92, 1.8, 0.88, 0.91, 0.82, 0.93, CURRENT_TIMESTAMP);

-- Recommendations
INSERT INTO recommendations (user_id, simulation_run_id, recommendation_text, recommendation_type, confidence, created_at) VALUES
(1, 1, 'This project aligns perfectly with your cloud architecture goals. Focus on strengthening ML fundamentals.', 'PRIMARY', 0.92, CURRENT_TIMESTAMP);
