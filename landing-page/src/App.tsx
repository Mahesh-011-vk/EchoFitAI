import { useState, useEffect } from 'react'
import { Navbar } from './components/Navbar'
import { DashboardView } from './components/DashboardView'
import { WorkoutView } from './components/WorkoutView'
import { DietView } from './components/DietView'
import { ProgressView } from './components/ProgressView'
import { CoachChatView } from './components/CoachChatView'
import { AccountView } from './components/AccountView'
import { MobileEmulatorView } from './components/MobileEmulatorView'
import { RoutineModal } from './components/RoutineModal'
import { ManualMealModal } from './components/ManualMealModal'
import { VideoPlayerModal } from './components/VideoPlayerModal'
import { ProgramSessionModal } from './components/ProgramSessionModal'
import type {
  UserProfile,
  SavedRoutine,
  MealItem,
  WeightRecord,
  ProgressPhotoItem,
  CardioSession,
  GuidedProgram,
  RecoveryMetrics,
  CalendarDayItem
} from './types/fitness'
import {
  DEFAULT_ROUTINES,
  DEFAULT_GUIDED_PROGRAMS,
  INITIAL_RECOVERY_METRICS,
  INITIAL_CALENDAR_DAYS,
  analyzeGoal
} from './utils/aiEngine'

function App() {
  // Navigation & View Mode
  const [activeTab, setActiveTab] = useState<'DASHBOARD' | 'WORKOUTS' | 'DIET' | 'PROGRESS' | 'COACH' | 'ACCOUNT'>('DASHBOARD')
  const [viewMode, setViewMode] = useState<'desktop' | 'mobile'>('desktop')

  // Modals state
  const [isRoutineModalOpen, setIsRoutineModalOpen] = useState(false)
  const [isManualMealOpen, setIsManualMealOpen] = useState(false)
  const [activeVideoSession, setActiveVideoSession] = useState<CardioSession | null>(null)
  const [activeGuidedProgram, setActiveGuidedProgram] = useState<GuidedProgram | null>(null)

  // 1. User Profile State (Persisted in localStorage)
  const [userProfile, setUserProfile] = useState<UserProfile>(() => {
    const saved = localStorage.getItem('echofit_user_profile')
    if (saved) {
      try { return JSON.parse(saved) } catch (e) { /* fallback */ }
    }
    return {
      name: 'Mahesh Sharma',
      age: 24,
      gender: 'Male',
      height: 178,
      weight: 74.5,
      targetWeight: 72.0,
      fitnessGoal: 'Muscle Gain',
      budgetLevel: 'Medium',
      dietType: 'Vegetarian',
      gymAccess: 'Both',
      experienceLevel: 'Intermediate',
      preferredRestDay: 'Sunday',
      workoutTime: '07:00 PM',
      injuriesOrLimitations: 'None'
    }
  })

  useEffect(() => {
    localStorage.setItem('echofit_user_profile', JSON.stringify(userProfile))
  }, [userProfile])

  // Recalculated Goal Analysis using EchoFit AI Engine
  const goalAnalysis = analyzeGoal(userProfile)

  // 2. Recovery Metrics State (Image 1 "Recovery today 86%")
  const [recoveryMetrics, setRecoveryMetrics] = useState<RecoveryMetrics>(() => {
    const saved = localStorage.getItem('echofit_recovery_metrics')
    if (saved) {
      try { return JSON.parse(saved) } catch (e) { /* fallback */ }
    }
    return INITIAL_RECOVERY_METRICS
  })

  useEffect(() => {
    localStorage.setItem('echofit_recovery_metrics', JSON.stringify(recoveryMetrics))
  }, [recoveryMetrics])

  // 3. Guided Programs (Image 1 Morning Reset, Deep Focus, etc.)
  const [guidedPrograms, setGuidedPrograms] = useState<GuidedProgram[]>(() => {
    const saved = localStorage.getItem('echofit_guided_programs')
    if (saved) {
      try { return JSON.parse(saved) } catch (e) { /* fallback */ }
    }
    return DEFAULT_GUIDED_PROGRAMS
  })

  useEffect(() => {
    localStorage.setItem('echofit_guided_programs', JSON.stringify(guidedPrograms))
  }, [guidedPrograms])

  // 4. Calendar Days Strip (Image 1 dates with colored activity dots)
  const [calendarDays, setCalendarDays] = useState<CalendarDayItem[]>(INITIAL_CALENDAR_DAYS)
  const [selectedDate, setSelectedDate] = useState<number>(15)

  // 5. Saved Routines State (Persisted in localStorage)
  const [savedRoutines, setSavedRoutines] = useState<SavedRoutine[]>(() => {
    const saved = localStorage.getItem('echofit_saved_routines')
    if (saved) {
      try { return JSON.parse(saved) } catch (e) { /* fallback */ }
    }
    return DEFAULT_ROUTINES
  })

  useEffect(() => {
    localStorage.setItem('echofit_saved_routines', JSON.stringify(savedRoutines))
  }, [savedRoutines])

  // 6. Active Routine Selection
  const [activeRoutineId, setActiveRoutineId] = useState<string>(() => {
    return localStorage.getItem('echofit_active_routine_id') || savedRoutines[0]?.id || 'routine-ppl'
  })

  useEffect(() => {
    localStorage.setItem('echofit_active_routine_id', activeRoutineId)
  }, [activeRoutineId])

  const activeRoutine = savedRoutines.find(r => r.id === activeRoutineId) || savedRoutines[0] || DEFAULT_ROUTINES[0]

  const handleSetActiveRoutine = (routine: SavedRoutine) => {
    setActiveRoutineId(routine.id)
  }

  // 7. Daily Logged Meals State (Persisted in localStorage)
  const [mealsLogged, setMealsLogged] = useState<MealItem[]>(() => {
    const saved = localStorage.getItem('echofit_logged_meals')
    if (saved) {
      try { return JSON.parse(saved) } catch (e) { /* fallback */ }
    }
    return [
      { id: '1', name: 'Sprouts Salad with Roasted Chana', portion: '1 Bowl', calories: 280, protein: 18, carbs: 38, fats: 4, category: 'Breakfast' },
      { id: '2', name: 'Paneer Tikka with 2 Whole Wheat Rotis', portion: '1 Plate', calories: 510, protein: 32, carbs: 48, fats: 19, category: 'Lunch' }
    ]
  })

  useEffect(() => {
    localStorage.setItem('echofit_logged_meals', JSON.stringify(mealsLogged))
  }, [mealsLogged])

  const handleAddMeal = (meal: Omit<MealItem, 'id'>) => {
    const newMeal: MealItem = {
      ...meal,
      id: Date.now().toString(),
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    }
    setMealsLogged(prev => [...prev, newMeal])
  }

  const handleRemoveMeal = (mealId: string) => {
    setMealsLogged(prev => prev.filter(m => m.id !== mealId))
  }

  // 8. Water Hydration State
  const [waterGlasses, setWaterGlasses] = useState<number>(() => {
    const saved = localStorage.getItem('echofit_water_glasses')
    return saved ? parseInt(saved) || 6 : 6
  })

  useEffect(() => {
    localStorage.setItem('echofit_water_glasses', waterGlasses.toString())
  }, [waterGlasses])

  // 9. Workout Completion State
  const [workoutCompleted, setWorkoutCompleted] = useState<boolean>(() => {
    return localStorage.getItem('echofit_workout_completed') === 'true'
  })

  useEffect(() => {
    localStorage.setItem('echofit_workout_completed', workoutCompleted.toString())
  }, [workoutCompleted])

  // 10. Streak Count
  const streakCount = 5

  // 11. Weight Logs History
  const [weightLogs, setWeightLogs] = useState<WeightRecord[]>(() => {
    const saved = localStorage.getItem('echofit_weight_logs')
    if (saved) {
      try { return JSON.parse(saved) } catch (e) { /* fallback */ }
    }
    return [
      { date: 'Mon', weight: 75.8 },
      { date: 'Tue', weight: 75.3 },
      { date: 'Wed', weight: 75.0 },
      { date: 'Thu', weight: 74.8 },
      { date: 'Today', weight: 74.5 }
    ]
  })

  useEffect(() => {
    localStorage.setItem('echofit_weight_logs', JSON.stringify(weightLogs))
  }, [weightLogs])

  const handleAddWeightLog = (val: number) => {
    const newRecord: WeightRecord = {
      date: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
      weight: val
    }
    setWeightLogs(prev => [...prev, newRecord])
    setUserProfile(prev => ({ ...prev, weight: val }))
  }

  // 12. Photos Check-in List
  const [photosList, setPhotosList] = useState<ProgressPhotoItem[]>([
    {
      id: 'photo_1',
      position: 'Front',
      url: 'https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=600&q=80',
      date: 'This Week'
    }
  ])

  const handleAddPhoto = (position: 'Front' | 'Side' | 'Back', url: string) => {
    setPhotosList(prev => [
      ...prev.filter(p => p.position !== position),
      {
        id: `photo_${Date.now()}`,
        position,
        url,
        date: 'Today'
      }
    ])
  }

  // Handle Save Custom Routine from Modal
  const handleSaveCustomRoutine = (newRoutine: SavedRoutine) => {
    setSavedRoutines(prev => [newRoutine, ...prev])
    setActiveRoutineId(newRoutine.id)
  }

  // Handle Complete Video Studio Session
  const handleCompleteVideoSession = () => {
    if (activeVideoSession) {
      setWorkoutCompleted(true)
      setActiveVideoSession(null)
      alert(`🎉 Congratulations! You completed the ${activeVideoSession.name} session and burned ~${activeVideoSession.caloriesBurned} kcal!`)
    }
  }

  // Handle Complete Guided Program (Image 1)
  const handleCompleteProgram = (programId: string) => {
    setGuidedPrograms(prev =>
      prev.map(p =>
        p.id === programId
          ? { ...p, completedSessions: Math.min(p.totalSessions, p.completedSessions + 1) }
          : p
      )
    )
    // Award recovery boost
    setRecoveryMetrics(prev => ({
      ...prev,
      score: Math.min(99, prev.score + 3),
      status: 'Optimal Readiness'
    }))
    setActiveGuidedProgram(null)
    alert(`✨ Program Completed! +3% Recovery Readiness boost recorded for today.`)
  }

  return (
    <div className="min-h-screen w-full bg-[#F8F5EE] text-[#231815] font-sans flex flex-col relative overflow-x-hidden selection:bg-[#721C28] selection:text-white">
      
      {/* Ambient warm champagne & subtle burgundy glows */}
      <div className="fixed w-[650px] h-[650px] bg-[#E8DCBF]/50 blur-[160px] rounded-full top-[-150px] left-[-150px] pointer-events-none z-0" />
      <div className="fixed w-[700px] h-[700px] bg-[#721C28]/6 blur-[190px] rounded-full bottom-[-180px] right-[-150px] pointer-events-none z-0" />

      {/* Navigation Header */}
      <Navbar
        activeTab={activeTab}
        setActiveTab={setActiveTab}
        userProfile={userProfile}
        streakCount={streakCount}
        viewMode={viewMode}
        setViewMode={setViewMode}
        onOpenAccount={() => setActiveTab('ACCOUNT')}
      />

      {/* Main Content Area */}
      <main className="flex-1 w-full px-4 sm:px-6 lg:px-8 py-6 relative z-10">
        {viewMode === 'desktop' ? (
          <>
            {activeTab === 'DASHBOARD' && (
              <DashboardView
                userProfile={userProfile}
                goalAnalysis={goalAnalysis}
                mealsLogged={mealsLogged}
                waterGlasses={waterGlasses}
                setWaterGlasses={setWaterGlasses}
                workoutCompleted={workoutCompleted}
                setWorkoutCompleted={setWorkoutCompleted}
                activeRoutine={activeRoutine}
                streakCount={streakCount}
                onNavigate={tab => setActiveTab(tab as any)}
                onOpenManualMeal={() => setIsManualMealOpen(true)}
                recoveryMetrics={recoveryMetrics}
                setRecoveryMetrics={setRecoveryMetrics}
                guidedPrograms={guidedPrograms}
                onStartGuidedProgram={prog => setActiveGuidedProgram(prog)}
                calendarDays={calendarDays}
                selectedDate={selectedDate}
                setSelectedDate={setSelectedDate}
              />
            )}

            {activeTab === 'WORKOUTS' && (
              <WorkoutView
                activeRoutine={activeRoutine}
                workoutCompleted={workoutCompleted}
                setWorkoutCompleted={setWorkoutCompleted}
                onStartVideoSession={session => setActiveVideoSession(session)}
                onOpenRoutinesHub={() => setActiveTab('ACCOUNT')}
              />
            )}

            {activeTab === 'DIET' && (
              <DietView
                userProfile={userProfile}
                goalAnalysis={goalAnalysis}
                mealsLogged={mealsLogged}
                onAddMeal={handleAddMeal}
                onRemoveMeal={handleRemoveMeal}
                onOpenManualModal={() => setIsManualMealOpen(true)}
              />
            )}

            {activeTab === 'PROGRESS' && (
              <ProgressView
                userProfile={userProfile}
                weightLogs={weightLogs}
                onAddWeightLog={handleAddWeightLog}
                photosList={photosList}
                onAddPhoto={handleAddPhoto}
              />
            )}

            {activeTab === 'COACH' && (
              <CoachChatView
                userProfile={userProfile}
                goalAnalysis={goalAnalysis}
                activeRoutine={activeRoutine}
                mealsLogged={mealsLogged}
                waterGlasses={waterGlasses}
              />
            )}

            {activeTab === 'ACCOUNT' && (
              <AccountView
                userProfile={userProfile}
                setUserProfile={setUserProfile}
                goalAnalysis={goalAnalysis}
                savedRoutines={savedRoutines}
                setSavedRoutines={setSavedRoutines}
                activeRoutine={activeRoutine}
                setActiveRoutine={handleSetActiveRoutine}
                onOpenCreateRoutineModal={() => setIsRoutineModalOpen(true)}
              />
            )}
          </>
        ) : (
          /* Mobile Emulator View Mode (Exact replica of Image 1) */
          <MobileEmulatorView
            userProfile={userProfile}
            goalAnalysis={goalAnalysis}
            activeRoutine={activeRoutine}
            mealsLogged={mealsLogged}
            waterGlasses={waterGlasses}
            setWaterGlasses={setWaterGlasses}
            workoutCompleted={workoutCompleted}
            setWorkoutCompleted={setWorkoutCompleted}
            streakCount={streakCount}
            photosList={photosList}
            onOpenManualMeal={() => setIsManualMealOpen(true)}
            onOpenAccount={() => setActiveTab('ACCOUNT')}
            onStartVideoSession={session => setActiveVideoSession(session)}
            recoveryMetrics={recoveryMetrics}
            setRecoveryMetrics={setRecoveryMetrics}
            guidedPrograms={guidedPrograms}
            onStartGuidedProgram={prog => setActiveGuidedProgram(prog)}
            calendarDays={calendarDays}
            selectedDate={selectedDate}
            setSelectedDate={setSelectedDate}
          />
        )}
      </main>

      {/* Modals */}
      <RoutineModal
        isOpen={isRoutineModalOpen}
        onClose={() => setIsRoutineModalOpen(false)}
        onSaveRoutine={handleSaveCustomRoutine}
      />

      <ManualMealModal
        isOpen={isManualMealOpen}
        onClose={() => setIsManualMealOpen(false)}
        onSaveMeal={handleAddMeal}
      />

      <VideoPlayerModal
        session={activeVideoSession}
        onClose={() => setActiveVideoSession(null)}
        onComplete={handleCompleteVideoSession}
      />

      <ProgramSessionModal
        program={activeGuidedProgram}
        onClose={() => setActiveGuidedProgram(null)}
        onCompleteProgram={handleCompleteProgram}
      />

    </div>
  )
}

export default App
