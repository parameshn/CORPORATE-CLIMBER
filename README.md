# Corporate Climber

## Overview

Corporate Climber is an enterprise-grade Spring Boot application that leverages artificial intelligence and statistical modeling to predict project success probabilities and optimize career development trajectories. The platform utilizes Monte Carlo simulations, machine learning algorithms, and natural language processing to deliver data-driven project recommendations and career guidance.

## Architecture

### Core Technologies
- **Framework**: Spring Boot 3.2
- **Runtime**: Java 17
- **Database**: PostgreSQL (Production), H2 (Development)
- **Build System**: Maven 3.6+

### AI & Analytics Stack
- **AI Platform**: Google Gemini API
- **Natural Language Processing**: Google Cloud NLP
- **Simulation Engine**: Custom Monte Carlo implementation
- **Security**: Spring Security

## Features

### Statistical Analysis Engine
- Monte Carlo simulation with 10,000 trial iterations
- Statistical confidence interval calculations
- Success probability modeling with uncertainty quantification

### AI-Powered Intelligence
- Personalized career recommendation system
- Natural language processing for sentiment analysis
- Team compatibility scoring algorithms
- Skill gap identification and growth path optimization

### Real-Time Analytics
- Interactive dashboard for skill assessment visualization
- Emotional trend analysis and peer interaction metrics
- Project success probability tracking
- Career progression analytics

### Configuration Management
- Customizable priority weighting system
- Configurable simulation parameters
- Environment-specific configuration profiles

## System Requirements

### Prerequisites
- Java Development Kit 17 or higher
- Apache Maven 3.6 or higher
- PostgreSQL 14 or higher

### Hardware Specifications
- Minimum 4GB RAM
- 2 CPU cores
- 10GB available disk space

## Installation Guide

### 1. Repository Setup
```bash
git clone https://github.com/parameshn/corporate-climber.git
cd corporate-climber
```

### 2. Environment Configuration
Create `application.yml` configuration file:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/corporate_climber
    username: ${DB_USERNAME:your_username}
    password: ${DB_PASSWORD:your_password}
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 8080

gemini:
  api:
    key: ${GEMINI_API_KEY:your_gemini_api_key}
    base-url: https://generativelanguage.googleapis.com/v1beta
    timeout: 5000

google:
  credentials:
    path: ${GOOGLE_CREDENTIALS_PATH:/path/to/service-account-key.json}
  nlp:
    timeout: 3000

simulation:
  default-trials: 10000
  skill-growth-factor: 0.15
```

### 3. Database Setup
```sql
CREATE DATABASE corporate_climber;
CREATE USER corporate_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE corporate_climber TO corporate_user;
```

### 4. Application Deployment

#### Standard Deployment
```bash
mvn clean install
mvn spring-boot:run
```

## API Reference

### Base URL
```
http://localhost:8080/api/v1
```

### Authentication
All API endpoints are secured with Spring Security. Authentication details will be provided in the API documentation.

### Core Endpoints

#### User Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/users` | Create user profile |
| GET | `/users/{userId}` | Retrieve user profile |

#### Peer Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/peers/department/{department}` | Get peers by department |
| POST | `/peers/{peerId}/skills` | Add skill to peer |

#### Project Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/projects` | Get all projects |
| GET | `/projects/{projectId}` | Get specific project |
| GET | `/projects/domain/{domain}` | Get projects by domain |
| POST | `/projects` | Create new project |

#### Project Simulation
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/simulate` | Execute Monte Carlo simulation |
| GET | `/users/{userId}/simulations` | Retrieve simulation history |

#### Analytics & Insights
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/conversations` | Analyze conversation sentiment |
| GET | `/users/{userId}/interactions` | Retrieve peer interaction scores |
| GET | `/users/{userId}/recommendations` | Get AI-generated recommendations |
| GET | `/recommendations/{id}` | Get specific recommendation |

#### Data Analytics
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/users/{userId}/skill-vector` | Get user skill vector |
| GET | `/projects/{projectId}/requirement-vector` | Get project requirement vector |

#### Health Check
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health` | Application health status |

### Sample Requests

#### Project Simulation Request
```bash
curl -X POST "http://localhost:8080/api/v1/simulate" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "candidateProjectIds": [1, 2, 3],
    "priorityWeights": {
      "SKILL_GROWTH": 0.5,
      "PEER_SYNERGY": 0.3,
      "GOAL_ALIGNMENT": 0.2
    },
    "simulationTrials": 10000
  }'
```

#### Conversation Analysis Request
```bash
curl -X POST "http://localhost:8080/api/v1/conversations" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "peerId": 2,
    "conversationText": "Discussion regarding API architecture and implementation strategy",
    "platform": "MEETING",
    "conversationDate": "2023-10-15T14:30:00"
  }'
```

### Response Format

#### Simulation Response
```json
{
  "simulationId": "uuid-string",
  "projectId": 1,
  "projectName": "Cloud Migration Initiative",
  "successProbability": 0.847,
  "confidenceInterval": {
    "lower": 0.792,
    "upper": 0.901
  },
  "metrics": {
    "expectedSkillGain": 1.8,
    "peerSynergyScore": 8.7,
    "goalAlignmentScore": 9.2
  },
  "recommendation": "This project demonstrates strong alignment with your cloud architecture career objectives and offers significant skill development opportunities.",
  "timestamp": "2023-10-15T10:30:00Z"
}
```

## Database Schema

### Core Tables
- `users` - User profile information
- `user_skills` - Individual skill assessments and proficiency levels
- `projects` - Project metadata and requirements
- `project_requirements` - Required skills and competency levels
- `conversation_records` - Communication logs and metadata
- `emotion_scores` - Sentiment analysis results
- `simulation_runs` - Monte Carlo simulation results
- `recommendations` - AI-generated career guidance
- `peer_interactions` - Team collaboration metrics
- `skill_growth_tracking` - Historical skill development data

## Configuration Parameters

### Application Properties
```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api/v1

# Database Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Simulation Engine
simulation.default-trials=10000
simulation.confidence-level=0.95
simulation.skill-growth-factor=0.15
simulation.max-concurrent-simulations=5

# AI Service Configuration
gemini.api.timeout=5000
gemini.api.retry-attempts=3
google.nlp.timeout=3000
google.nlp.batch-size=100
```

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn integration-test
```

### Load Testing
```bash
mvn gatling:test
```

## Monitoring & Observability

### Health Endpoints
- `/actuator/health` - Application health status
- `/actuator/metrics` - Performance metrics
- `/actuator/info` - Application information

### Logging Configuration
The application uses SLF4J with Logback for structured logging. Log levels can be configured per package in `application.yml`.

## Security

### Authentication & Authorization
- Spring Security implementation
- Role-based access control (RBAC)
- API rate limiting
- Request validation and sanitization

### Data Protection
- Database connection encryption
- Sensitive data masking in logs
- API key rotation support
- GDPR compliance features

## Contributing

### Development Workflow
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/enhancement-name`)
3. Implement changes with appropriate tests
4. Ensure code quality standards are met
5. Submit a pull request with detailed description

### Code Standards
- Follow Google Java Style Guide
- Maintain minimum 80% test coverage
- Document all public APIs
- Use conventional commit messages

## Support & Documentation

### Additional Resources
- **Complete API Documentation**: [Postman Collection](https://kxld-4969301.postman.co/workspace/Paramesh-workspace~638a0202-4881-45c9-8a40-544f0617cade/collection/44593529-2675fe79-4de0-4310-8d53-32bd0f045916?action=share&creator=44593529)
- **Issue Tracking**: GitHub Issues
- **Technical Support**: parameshdarshan1234@gmail.com

### Version Information
- **Current Version**: 1.0.0
- **Minimum Java Version**: 17
- **Spring Boot Version**: 3.2.x

## License

This project is licensed under the MIT License. See the [LICENSE.md](LICENSE.md) file for complete terms and conditions.

---

**Developed by**: Paramesh N  
**Contact**: [GitHub](https://github.com/parameshn) | [LinkedIn](https://www.linkedin.com/in/paramesh-n-70464b256)  
**Project Repository**: https://github.com/parameshn/corporate-climber
