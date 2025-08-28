-- Insert data
-- Sample data for testing
INSERT INTO users (email, first_name, last_name, department, role) VALUES 
('john.doe@corpclimb.com', 'John', 'Doe', 'Engineering', 'Senior Developer'),
('jane.smith@corpclimb.com', 'Jane', 'Smith', 'Engineering', 'Tech Lead'),
('mike.johnson@corpclimb.com', 'Mike', 'Johnson', 'Product', 'Product Manager');

INSERT INTO user_career_goals (user_id, goal) VALUES 
(1, 'Become a technical architect'),
(1, 'Lead large scale projects'),
(2, 'Transition to engineering management'),
(3, 'Develop AI/ML expertise');

INSERT INTO peers (name, email, department, role, skill_level) VALUES 
('Alice Williams', 'alice.williams@corpclimb.com', 'Engineering', 'Senior Developer', 8),
('Bob Chen', 'bob.chen@corpclimb.com', 'Engineering', 'Full Stack Developer', 7),
('Carol Davis', 'carol.davis@corpclimb.com', 'Data Science', 'ML Engineer', 9);

INSERT INTO managers (name, email, department, mentorship_rating, leadership_score) VALUES 
('David Wilson', 'david.wilson@corpclimb.com', 'Engineering', 8.5, 9.0),
('Sarah Thompson', 'sarah.thompson@corpclimb.com', 'Product', 7.8, 8.2);

INSERT INTO projects (name, description, domain, priority, duration_months, complexity_score) VALUES 
('AI Recommendation Engine', 'Build ML-powered recommendation system', 'AI/ML', 'HIGH', 6, 8),
('Mobile App Redesign', 'Complete redesign of mobile application', 'Mobile', 'MEDIUM', 4, 6),
('Cloud Migration', 'Migrate legacy systems to cloud', 'Cloud', 'HIGH', 8, 7),
('API Gateway Implementation', 'Implement enterprise API gateway', 'Backend', 'MEDIUM', 5, 7),
('Data Analytics Platform', 'Build real-time analytics platform', 'Data', 'HIGH', 9, 9);

INSERT INTO project_requirements (project_id, skill_name, required_level, importance_weight) VALUES 
(1, 'Python', 8, 1.0),
(1, 'Machine Learning', 7, 0.9),
(1, 'TensorFlow', 6, 0.8),
(2, 'React Native', 7, 1.0),
(2, 'UI/UX Design', 6, 0.7),
(3, 'AWS', 8, 1.0),
(3, 'Docker', 7, 0.8),
(3, 'Kubernetes', 6, 0.7);

INSERT INTO user_skills (user_id, skill_name, current_level, target_level) VALUES 
(1, 'Java', 8, 9),
(1, 'Python', 6, 8),
(1, 'AWS', 5, 7),
(1, 'Machine Learning', 4, 7),
(2, 'React', 9, 10),
(2, 'Node.js', 8, 9),
(2, 'Leadership', 6, 8);