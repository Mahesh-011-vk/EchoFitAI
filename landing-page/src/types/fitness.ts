export interface UserProfile {
  name: string
  age: number
  gender: 'Male' | 'Female'
  height: number // in cm
  weight: number // in kg
  targetWeight: number // in kg
  fitnessGoal: 'Fat Loss' | 'Muscle Gain' | 'Body Recomposition' | 'General Fitness'
  budgetLevel: 'Low' | 'Medium' | 'High'
  dietType: 'Vegetarian' | 'Eggetarian' | 'Non-Vegetarian' | 'Vegan'
  gymAccess: 'Gym' | 'Home' | 'Both'
  experienceLevel: 'Beginner' | 'Intermediate' | 'Advanced'
  preferredRestDay: string
  workoutTime: string
  injuriesOrLimitations: string
}

export interface GoalAnalysis {
  bmi: number
  bmiCategory: string
  estimatedBodyFat: number
  calorieTarget: number
  proteinTarget: number
  carbsTarget: number
  fatsTarget: number
  fiberTarget: number
  waterIntakeTargetLiters: number
  suggestedWeightGoal: number
  thirtyDayMission: string
  fitnessScore: number
}

export interface ExerciseItem {
  id: string
  name: string
  sets: number
  reps: string
  targetMuscle: string
  equipment: string
  instructions: string[]
  commonMistakes: string[]
  alternative1: string
  alternative2: string
  isCompleted?: boolean
}

export interface WorkoutSplitDay {
  dayIndex: number
  name: string
  focus: string
  exercises: ExerciseItem[]
}

export interface SavedRoutine {
  id: string
  name: string
  description: string
  focus: string
  daysPerWeek: number
  isCustom: boolean
  createdAt: string
  splits: WorkoutSplitDay[]
}

export interface MealItem {
  id: string
  name: string
  portion: string
  calories: number
  protein: number
  carbs: number
  fats: number
  category: 'Breakfast' | 'Lunch' | 'Snacks' | 'Dinner'
  time?: string
}

export interface WeightRecord {
  date: string
  weight: number
}

export interface ProgressPhotoItem {
  id: string
  position: 'Front' | 'Side' | 'Back'
  url: string
  date: string
}

export interface CardioSession {
  id: string
  name: string
  durationMinutes: number
  caloriesBurned: number
  difficulty: 'Easy' | 'Medium' | 'Hard'
  category: 'Yoga' | 'Zumba'
  benefits: string
  steps: string[]
}

export interface ChatMessage {
  id: string
  sender: 'ai' | 'user'
  text: string
  timestamp: string
}

export interface GuidedProgram {
  id: string
  title: string
  subtitle: string
  completedSessions: number
  totalSessions: number
  durationMinutes: number
  benefits: string
  steps: string[]
  auraColor: string
}

export interface RecoveryMetrics {
  score: number // e.g. 86
  status: string // e.g. "Optimal Readiness"
  sleepHours: number
  hrvMs: number
  sorenessLevel: 'Low' | 'Moderate' | 'High'
  trend: number[]
}

export interface CalendarDayItem {
  date: number
  dayName: string
  dots: string[] // hex colors for indicators
  isToday: boolean
}
