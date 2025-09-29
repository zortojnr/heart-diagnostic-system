# 🫀 Heart Diagnostic System

An AI-powered heart disease diagnostic system built with Vue.js frontend, Spring Boot backend, and machine learning models for accurate cardiovascular risk assessment.

## 🏗️ System Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Vue.js        │    │   Spring Boot   │    │   ML Models     │
│   Frontend      │◄──►│   Backend       │◄──►│   (Weka)        │
│   (Port 3000)   │    │   (Port 8080)   │    │   Heart Model   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Nginx         │    │   Docker        │    │   Firebase      │
│   Reverse Proxy │    │   Containers    │    │   Auth & Data   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Quick Start

### Prerequisites

- **Node.js** 18+ and npm 9+
- **Java** 17+ (OpenJDK recommended)
- **Docker** and Docker Compose (for containerized deployment)
- **Git** for version control

### 🔧 Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/heart-diagnostic-system.git
   cd heart-diagnostic-system
   ```

2. **Install dependencies**
   ```bash
   npm install
   npm run install:all
   ```

3. **Configure environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

4. **Start development servers**
   ```bash
   npm run dev
   ```

   This will start:
   - Frontend: http://localhost:5173
   - Backend: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html

### 🐳 Docker Deployment

#### Development Environment
```bash
docker-compose up -d
```

#### Production Environment
```bash
docker-compose -f docker-compose.prod.yml up -d
```

## 📁 Project Structure

```
heart-diagnostic-system/
├── 📁 frontend/                 # Vue.js frontend application
│   ├── 📁 src/
│   │   ├── 📁 components/       # Reusable Vue components
│   │   ├── 📁 views/           # Page components
│   │   ├── 📁 stores/          # Pinia state management
│   │   ├── 📁 router/          # Vue Router configuration
│   │   └── 📁 types/           # TypeScript type definitions
│   ├── 📄 Dockerfile           # Development Docker image
│   ├── 📄 Dockerfile.prod      # Production Docker image
│   └── 📄 nginx.conf           # Nginx configuration
├── 📁 backend/                  # Spring Boot backend application
│   ├── 📁 src/main/java/       # Java source code
│   ├── 📁 src/main/resources/  # Application resources
│   ├── 📄 Dockerfile           # Development Docker image
│   ├── 📄 Dockerfile.prod      # Production Docker image
│   └── 📄 pom.xml              # Maven configuration
├── 📁 ml-models/               # Machine learning models and datasets
│   ├── 📁 datasets/            # Training datasets
│   └── 📁 exported-models/     # Trained model files
├── 📁 infra/                   # Infrastructure configuration
│   ├── 📄 nginx.conf           # Nginx reverse proxy config
│   ├── 📄 prometheus.yml       # Monitoring configuration
│   └── 📄 firebase.rules.json  # Firebase security rules
├── 📄 docker-compose.yml       # Development Docker Compose
├── 📄 docker-compose.prod.yml  # Production Docker Compose
├── 📄 package.json             # Root package configuration
└── 📄 README.md                # This file
```

## 🔌 API Endpoints

### Health Check
- **GET** `/api/health` - Service health status

### Diagnosis
- **POST** `/api/diagnose` - Submit symptoms for diagnosis

#### Request Body Example:
```json
{
  "age": 45,
  "gender": "M",
  "chestPainType": 2,
  "restingBloodPressure": 130,
  "serumCholesterol": 250,
  "fastingBloodSugar": false,
  "restingECG": 1,
  "maxHeartRate": 150,
  "exerciseInducedAngina": false,
  "stDepression": 1.5,
  "slopeOfPeakExercise": 2,
  "numberOfMajorVessels": 1,
  "thalassemia": 3
}
```

#### Response Example:
```json
{
  "label": "Moderate Risk",
  "scores": {
    "Healthy": 0.25,
    "Moderate Risk": 0.65,
    "Severe Risk": 0.10
  },
  "explanation": "Based on the provided symptoms, moderate cardiovascular risk factors have been identified.",
  "modelVersion": "1.0.0",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## 🏥 Medical Data Fields

| Field | Description | Type | Range/Values |
|-------|-------------|------|--------------|
| **age** | Patient age in years | Integer | 18-100 |
| **gender** | Patient gender | String | "M", "F" |
| **chestPainType** | Type of chest pain | Integer | 0-3 (0: Typical angina, 1: Atypical angina, 2: Non-anginal pain, 3: Asymptomatic) |
| **restingBloodPressure** | Resting blood pressure (mmHg) | Integer | 80-200 |
| **serumCholesterol** | Serum cholesterol (mg/dl) | Integer | 100-600 |
| **fastingBloodSugar** | Fasting blood sugar > 120 mg/dl | Boolean | true/false |
| **restingECG** | Resting ECG results | Integer | 0-2 (0: Normal, 1: ST-T wave abnormality, 2: Left ventricular hypertrophy) |
| **maxHeartRate** | Maximum heart rate achieved | Integer | 60-220 |
| **exerciseInducedAngina** | Exercise induced angina | Boolean | true/false |
| **stDepression** | ST depression induced by exercise | Float | 0.0-6.2 |
| **slopeOfPeakExercise** | Slope of peak exercise ST segment | Integer | 0-2 (0: Upsloping, 1: Flat, 2: Downsloping) |
| **numberOfMajorVessels** | Number of major vessels colored by fluoroscopy | Integer | 0-4 |
| **thalassemia** | Thalassemia type | Integer | 0-3 (0: Normal, 1: Fixed defect, 2: Reversible defect, 3: Not described) |

## 🎯 Risk Categories

- **🟢 Healthy**: Low risk of heart disease (Score: 0.0-0.33)
- **🟡 Moderate Risk**: Some risk factors present (Score: 0.34-0.66)
- **🔴 Severe Risk**: High risk, immediate attention needed (Score: 0.67-1.0)

## 🛠️ Available Scripts

### Root Level Commands
```bash
npm run dev              # Start both frontend and backend in development
npm run build            # Build both frontend and backend for production
npm run test             # Run all tests
npm run install:all      # Install all dependencies
npm run clean            # Clean all build artifacts
npm run docker:build     # Build Docker images
npm run docker:up        # Start Docker containers
npm run docker:down      # Stop Docker containers
npm run deploy:prod      # Deploy to production
```

### Frontend Commands
```bash
npm run dev:frontend     # Start frontend development server
npm run build:frontend   # Build frontend for production
npm run test:frontend    # Run frontend tests
npm run lint             # Lint frontend code
npm run preview          # Preview production build
```

### Backend Commands
```bash
npm run dev:backend      # Start backend development server
npm run build:backend    # Build backend JAR file
npm run test:backend     # Run backend tests
```

## 🧪 Testing

### Frontend Testing
```bash
cd frontend
npm run test            # Run unit tests
npm run test:coverage   # Run tests with coverage
npm run test:e2e        # Run end-to-end tests
```

### Backend Testing
```bash
cd backend
./mvnw test            # Run unit tests
./mvnw integration-test # Run integration tests
./mvnw verify          # Run all tests with verification
```

## 🔧 Configuration

### Environment Variables

Copy `.env.example` to `.env` and configure:

- **Frontend**: API URLs, Firebase config, app settings
- **Backend**: Database, ML models, security settings
- **Docker**: Container orchestration settings

### Firebase Setup

1. Create a Firebase project
2. Enable Authentication and Firestore
3. Add your web app configuration to `.env`
4. Configure security rules in `infra/firebase.rules.json`

### ML Model Configuration

The system uses Weka-trained models located in `ml-models/exported-models/`:
- `heart-model.model` - Main classification model
- `label-encoder.joblib` - Label encoding for categorical features

## 🚀 Deployment

### Production Deployment with Docker

1. **Build and deploy**
   ```bash
   npm run deploy:prod
   ```

2. **Monitor services**
   ```bash
   docker-compose -f docker-compose.prod.yml logs -f
   ```

3. **Scale services** (if needed)
   ```bash
   docker-compose -f docker-compose.prod.yml up -d --scale backend=3
   ```

### Manual Deployment

1. **Build applications**
   ```bash
   npm run build
   ```

2. **Deploy frontend** (to CDN/static hosting)
   ```bash
   # Upload frontend/dist/ to your hosting provider
   ```

3. **Deploy backend** (to application server)
   ```bash
   # Deploy backend/target/*.jar to your server
   java -jar heart-diagnostic-backend.jar
   ```

## 📊 Monitoring

### Health Checks
- Frontend: `http://localhost:3000/health`
- Backend: `http://localhost:8080/api/health`

### Metrics (Production)
- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3001`

## 🔒 Security

### Security Features
- ✅ CORS protection
- ✅ XSS protection headers
- ✅ Content Security Policy
- ✅ Rate limiting
- ✅ Input validation
- ✅ Secure headers
- ✅ Non-root Docker containers

### Security Best Practices
- Keep dependencies updated
- Use environment variables for secrets
- Enable HTTPS in production
- Regular security audits
- Monitor for vulnerabilities

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines
- Follow TypeScript/Java coding standards
- Write tests for new features
- Update documentation
- Ensure Docker builds pass
- Test in both development and production modes

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ⚠️ Medical Disclaimer

**IMPORTANT**: This system is for educational and research purposes only. It should not be used as a substitute for professional medical advice, diagnosis, or treatment. Always consult with qualified healthcare professionals for medical decisions.

## 🆘 Support

- 📧 Email: support@heartdiagnostic.com
- 📖 Documentation: [docs.heartdiagnostic.com](https://docs.heartdiagnostic.com)
- 🐛 Issues: [GitHub Issues](https://github.com/your-org/heart-diagnostic-system/issues)
- 💬 Discussions: [GitHub Discussions](https://github.com/your-org/heart-diagnostic-system/discussions)

---

Made with ❤️ by the Heart Diagnostic Team