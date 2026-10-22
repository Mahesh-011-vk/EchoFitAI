import { useState, useEffect } from 'react'
import {
  Dumbbell,
  Timer,
  Play,
  RotateCcw,
  CheckCircle2,
  RefreshCw,
  Flame,
  ChevronDown,
  ChevronUp
} from 'lucide-react'
import type { SavedRoutine, ExerciseItem, CardioSession } from '../types/fitness'
import { CARDIO_SESSIONS } from '../utils/aiEngine'

interface WorkoutViewProps {
  activeRoutine: SavedRoutine
  workoutCompleted: boolean
  setWorkoutCompleted: (completed: boolean) => void
  onStartVideoSession: (session: CardioSession) => void
  onOpenRoutinesHub: () => void
}

export const WorkoutView: React.FC<WorkoutViewProps> = ({
  activeRoutine,
  workoutCompleted,
  setWorkoutCompleted,
  onStartVideoSession,
  onOpenRoutinesHub
}) => {
  const [selectedSplitIndex, setSelectedSplitIndex] = useState(0)
  const [activeExercises, setActiveExercises] = useState<ExerciseItem[]>([])
  const [expandedExerciseId, setExpandedExerciseId] = useState<string | null>(null)
  
  // Studio sub-tab: 'WORKOUT' or 'YOGA_ZUMBA'
  const [subView, setSubView] = useState<'WORKOUT' | 'YOGA_ZUMBA'>('WORKOUT')

  // Rest Timer State
  const [timerSeconds, setTimerSeconds] = useState(60)
  const [isTimerRunning, setIsTimerRunning] = useState(false)
  const [initialTimerDuration, setInitialTimerDuration] = useState(60)

  // Sync exercises with selected split
  useEffect(() => {
    if (activeRoutine.splits && activeRoutine.splits[selectedSplitIndex]) {
      setActiveExercises(activeRoutine.splits[selectedSplitIndex].exercises)
    }
  }, [activeRoutine, selectedSplitIndex])

  // Rest Timer countdown
  useEffect(() => {
    let interval: any
    if (isTimerRunning && timerSeconds > 0) {
      interval = setInterval(() => {
        setTimerSeconds(s => s - 1)
      }, 1000)
    } else if (timerSeconds === 0 && isTimerRunning) {
      setIsTimerRunning(false)
      // Visual feedback / beep
      if (typeof window !== 'undefined' && 'Notification' in window && Notification.permission === 'granted') {
        new Notification("EchoFit AI: Rest Interval Finished!", { body: "Time for your next set. Push hard!" })
      }
    }
    return () => clearInterval(interval)
  }, [isTimerRunning, timerSeconds])

  const handleStartTimer = (duration: number) => {
    setInitialTimerDuration(duration)
    setTimerSeconds(duration)
    setIsTimerRunning(true)
  }

  const handleToggleExerciseDone = (exerciseId: string) => {
    setActiveExercises(prev =>
      prev.map(ex => (ex.id === exerciseId ? { ...ex, isCompleted: !ex.isCompleted } : ex))
    )
  }

  // Swap exercise with alternative
  const handleSwapAlternative = (exerciseId: string, alternativeName: string) => {
    setActiveExercises(prev =>
      prev.map(ex => {
        if (ex.id === exerciseId) {
          const oldName = ex.name
          return {
            ...ex,
            name: alternativeName,
            alternative1: oldName
          }
        }
        return ex
      })
    )
  }

  const currentSplit = activeRoutine.splits[selectedSplitIndex] || {
    name: 'Push Day',
    focus: 'Chest, Shoulders & Triceps',
    exercises: []
  }

  const completedCount = activeExercises.filter(ex => ex.isCompleted).length

  return (
    <div className="flex flex-col gap-6 w-full max-w-7xl mx-auto pb-12 animate-in fade-in duration-300">
      
      {/* Header and Routine Controls */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 classic-card p-6 rounded-3xl shadow-sm">
        <div>
          <div className="flex items-center gap-2">
            <span className="text-[10px] font-extrabold text-[#721C28] bg-[#721C28]/10 border border-[#721C28]/25 px-3 py-0.5 rounded-full uppercase tracking-wider">
              {activeRoutine.focus}
            </span>
            <span className="text-xs text-[#7A6E6B]">● {activeRoutine.daysPerWeek} Training Days</span>
          </div>
          <h1 className="font-serif font-black text-2xl sm:text-3xl text-[#231815] tracking-tight mt-1">
            {activeRoutine.name}
          </h1>
          <p className="text-[#5C4F4D] text-xs sm:text-sm mt-0.5">
            {activeRoutine.description}
          </p>
        </div>

        <div className="flex flex-wrap items-center gap-3">
          {/* LIQUID FROSTED GLASS SUB-VIEW SWITCHER (MATCHING USER'S IMAGE) */}
          <div className="flex items-center p-1 rounded-full liquid-glass-capsule shadow-md">
            <button
              onClick={() => setSubView('WORKOUT')}
              className={`px-4 py-2 rounded-full text-xs transition-all cursor-pointer ${
                subView === 'WORKOUT'
                  ? 'liquid-tab-active shadow-md text-[#721C28] font-black'
                  : 'liquid-tab-idle font-semibold'
              }`}
            >
              Strength & Splits
            </button>
            <button
              onClick={() => setSubView('YOGA_ZUMBA')}
              className={`px-4 py-2 rounded-full text-xs transition-all cursor-pointer ${
                subView === 'YOGA_ZUMBA'
                  ? 'liquid-tab-active shadow-md text-[#721C28] font-black'
                  : 'liquid-tab-idle font-semibold'
              }`}
            >
              Yoga & Dance Studio
            </button>
          </div>

          {/* Manage stored routines button */}
          <button
            onClick={onOpenRoutinesHub}
            className="flex items-center gap-1.5 bg-[#FAF7F2] hover:bg-white border border-[#E5DDD0] text-[#3D3130] hover:text-[#721C28] px-4 py-2.5 rounded-full text-xs font-bold cursor-pointer transition-all shadow-sm"
          >
            <Dumbbell className="w-3.5 h-3.5 text-[#721C28]" />
            <span>Manage Stored Routines</span>
          </button>
        </div>
      </div>

      {subView === 'WORKOUT' ? (
        <>
          {/* Split Day Selector Pills - LIQUID GLASS ACCENTS */}
          <div className="flex items-center gap-2 overflow-x-auto pb-2 scrollbar-none">
            {activeRoutine.splits.map((split, idx) => (
              <button
                key={split.dayIndex}
                onClick={() => setSelectedSplitIndex(idx)}
                className={`px-5 py-2.5 rounded-full text-xs font-black whitespace-nowrap cursor-pointer transition-all border shadow-sm ${
                  selectedSplitIndex === idx
                    ? 'liquid-tab-active text-[#721C28]'
                    : 'bg-white/80 hover:bg-white text-[#3D3130] hover:text-[#721C28] border-[#E5DDD0]'
                }`}
              >
                Day {idx + 1}: {split.name}
              </button>
            ))}
          </div>

          {/* Rest Interval Timer Bar */}
          <div className={`p-5 rounded-3xl border transition-all flex flex-col sm:flex-row items-center justify-between gap-4 ${
            isTimerRunning
              ? 'bg-[#721C28]/10 border-[#721C28]/40 shadow-lg shadow-[#721C28]/10 ring-1 ring-[#721C28]/25'
              : 'classic-card'
          }`}>
            <div className="flex items-center gap-3">
              <div className={`w-10 h-10 rounded-2xl flex items-center justify-center ${
                isTimerRunning ? 'bg-[#721C28] text-white animate-pulse' : 'bg-[#FAF7F2] border border-[#E5DDD0] text-[#721C28]'
              }`}>
                <Timer className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-xs font-black uppercase tracking-wider text-[#231815]">Rest Interval Countdown</h3>
                <p className="text-[11px] text-[#7A6E6B] font-medium">Keep sets paced for optimal muscle hypertrophy</p>
              </div>
            </div>

            {/* Timer Clock & Controls */}
            <div className="flex items-center gap-4">
              <div className="font-mono font-black text-3xl sm:text-4xl text-[#721C28] tracking-tight">
                {String(Math.floor(timerSeconds / 60)).padStart(2, '0')}:{String(timerSeconds % 60).padStart(2, '0')}
              </div>

              {/* Quick Preset Buttons */}
              <div className="flex items-center gap-1.5">
                {[30, 60, 90].map(sec => (
                  <button
                    key={sec}
                    onClick={() => handleStartTimer(sec)}
                    className="bg-[#FAF7F2] hover:bg-white border border-[#E5DDD0] text-[#3D3130] px-3 py-1.5 rounded-full text-xs font-bold cursor-pointer transition-all shadow-sm"
                  >
                    {sec}s
                  </button>
                ))}
              </div>

              {/* Play / Pause / Reset */}
              <div className="flex items-center gap-2">
                <button
                  onClick={() => setIsTimerRunning(!isTimerRunning)}
                  className={`px-5 py-2 rounded-full text-xs font-black cursor-pointer transition-all shadow-md ${
                    isTimerRunning
                      ? 'bg-rose-600 hover:bg-rose-700 text-white'
                      : 'bg-[#721C28] hover:bg-[#50111A] text-white shadow-[#721C28]/25'
                  }`}
                >
                  {isTimerRunning ? 'PAUSE' : 'START REST'}
                </button>
                <button
                  onClick={() => {
                    setIsTimerRunning(false)
                    setTimerSeconds(initialTimerDuration)
                  }}
                  className="p-2 bg-[#FAF7F2] hover:bg-white border border-[#E5DDD0] rounded-full text-[#5C4F4D] hover:text-[#231815] cursor-pointer shadow-sm"
                  title="Reset Timer"
                >
                  <RotateCcw className="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>

          {/* Exercises Header & Session Complete Action */}
          <div className="flex justify-between items-center mt-2">
            <div>
              <h2 className="font-serif font-black text-xl text-[#231815] tracking-tight">
                {currentSplit.name} ({activeExercises.length} Exercises)
              </h2>
              <p className="text-xs text-[#7A6E6B] font-semibold">{currentSplit.focus}</p>
            </div>

            <div className="flex items-center gap-3">
              <span className="text-xs text-[#7A6E6B] font-bold">
                {completedCount} / {activeExercises.length} Done
              </span>
              <button
                onClick={() => setWorkoutCompleted(!workoutCompleted)}
                className={`px-5 py-2 rounded-full text-xs font-black cursor-pointer transition-all flex items-center gap-2 shadow-md ${
                  workoutCompleted
                    ? 'bg-emerald-500/15 border border-emerald-500/30 text-emerald-800'
                    : 'bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white shadow-[#721C28]/20'
                }`}
              >
                <CheckCircle2 className="w-4 h-4" />
                <span>{workoutCompleted ? '✓ Split Completed' : 'Finish Workout'}</span>
              </button>
            </div>
          </div>

          {/* Exercises List Cards */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            {activeExercises.map(ex => {
              const isExpanded = expandedExerciseId === ex.id
              return (
                <div
                  key={ex.id}
                  className={`p-5 rounded-3xl border transition-all flex flex-col justify-between gap-3 ${
                    ex.isCompleted
                      ? 'bg-emerald-500/5 border-emerald-500/25 shadow-sm'
                      : 'classic-card hover:border-[#721C28]/30 shadow-sm'
                  }`}
                >
                  <div className="flex justify-between items-start gap-3">
                    <div className="flex items-center gap-3">
                      <button
                        onClick={() => handleToggleExerciseDone(ex.id)}
                        className={`w-7 h-7 rounded-full flex items-center justify-center font-bold text-xs cursor-pointer border transition-all ${
                          ex.isCompleted
                            ? 'bg-emerald-600 border-emerald-600 text-white'
                            : 'bg-white border-[#DCD4C7] text-[#7A6E6B] hover:border-[#721C28]'
                        }`}
                      >
                        {ex.isCompleted ? '✓' : ''}
                      </button>
                      <div>
                        <h3 className={`font-serif font-black text-base text-[#231815] ${ex.isCompleted ? 'line-through text-[#8A7D79]' : ''}`}>
                          {ex.name}
                        </h3>
                        <p className="text-xs text-[#721C28] font-bold mt-0.5">
                          {ex.sets} Sets × {ex.reps} Reps
                        </p>
                      </div>
                    </div>

                    <button
                      onClick={() => setExpandedExerciseId(isExpanded ? null : ex.id)}
                      className="text-[#7A6E6B] hover:text-[#231815] p-1 rounded-full hover:bg-[#FAF7F2] cursor-pointer"
                      title="Form & Guide Details"
                    >
                      {isExpanded ? <ChevronUp className="w-4 h-4" /> : <ChevronDown className="w-4 h-4" />}
                    </button>
                  </div>

                  {/* Muscle & Equipment Badges */}
                  <div className="flex flex-wrap items-center gap-2 text-[10px] font-bold">
                    <span className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-1 rounded-full text-[#5C4F4D]">
                      🎯 {ex.targetMuscle}
                    </span>
                    <span className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-1 rounded-full text-[#5C4F4D]">
                      ⚡ {ex.equipment}
                    </span>
                  </div>

                  {/* Expanded Guide & Substitutions View */}
                  {isExpanded && (
                    <div className="mt-2 pt-3 border-t border-[#E8E1D5] flex flex-col gap-3 text-xs animate-in fade-in duration-200">
                      
                      {/* Step by Step Form Cues */}
                      <div>
                        <h4 className="font-extrabold text-[11px] text-[#721C28] uppercase tracking-wider mb-1">Form Instructions:</h4>
                        <ul className="list-disc list-inside text-[#5C4F4D] space-y-1 text-[11px]">
                          {ex.instructions.map((ins, i) => (
                            <li key={i}>{ins}</li>
                          ))}
                        </ul>
                      </div>

                      {/* Common Mistakes */}
                      <div>
                        <h4 className="font-extrabold text-[11px] text-rose-700 uppercase tracking-wider mb-1">Common Mistakes to Avoid:</h4>
                        <ul className="list-disc list-inside text-[#5C4F4D] space-y-1 text-[11px]">
                          {ex.commonMistakes.map((mis, i) => (
                            <li key={i}>{mis}</li>
                          ))}
                        </ul>
                      </div>

                      {/* Alternative Swapping Feature */}
                      <div className="bg-[#FAF7F2] p-3 rounded-2xl border border-[#E5DDD0]">
                        <span className="text-[10px] font-extrabold text-[#7A6E6B] uppercase flex items-center gap-1.5 mb-2">
                          <RefreshCw className="w-3 h-3 text-[#721C28]" /> Swap With Alternative Exercise:
                        </span>
                        <div className="flex flex-wrap gap-2">
                          <button
                            onClick={() => handleSwapAlternative(ex.id, ex.alternative1)}
                            className="bg-white hover:bg-[#F3EFE6] border border-[#E5DDD0] px-3 py-1.5 rounded-full text-[11px] font-bold text-[#721C28] cursor-pointer transition-all shadow-sm"
                          >
                            ↔ {ex.alternative1}
                          </button>
                          <button
                            onClick={() => handleSwapAlternative(ex.id, ex.alternative2)}
                            className="bg-white hover:bg-[#F3EFE6] border border-[#E5DDD0] px-3 py-1.5 rounded-full text-[11px] font-bold text-[#721C28] cursor-pointer transition-all shadow-sm"
                          >
                            ↔ {ex.alternative2}
                          </button>
                        </div>
                      </div>

                    </div>
                  )}
                </div>
              )
            })}
          </div>
        </>
      ) : (
        /* Yoga & Dance Studio View */
        <div className="flex flex-col gap-6 animate-in fade-in duration-300">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="font-serif font-black text-2xl text-[#231815] tracking-tight">
                Yoga & Cardio Dance Studio
              </h2>
              <p className="text-xs text-[#7A6E6B] font-semibold">
                Active recovery flows, back decompression, and high-energy Latin dance HIIT
              </p>
            </div>
            <span className="text-xs font-bold text-[#721C28] bg-[#721C28]/10 px-3.5 py-1.5 rounded-full border border-[#721C28]/25">
              6 Studio Classes Available
            </span>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {CARDIO_SESSIONS.map(session => (
              <div
                key={session.id}
                className="classic-card hover:border-[#721C28]/40 p-5 rounded-3xl flex flex-col justify-between gap-4 transition-all group shadow-sm hover:shadow-md"
              >
                <div>
                  <div className="flex justify-between items-center mb-2">
                    <span className={`text-[10px] font-extrabold px-3 py-1 rounded-full uppercase tracking-wider ${
                      session.category === 'Yoga' ? 'bg-cyan-500/15 text-cyan-800 border border-cyan-500/25' : 'bg-[#721C28]/10 text-[#721C28] border border-[#721C28]/20'
                    }`}>
                      {session.category} ● {session.difficulty}
                    </span>
                    <span className="text-xs font-extrabold text-[#721C28] flex items-center gap-1">
                      <Flame className="w-3.5 h-3.5 fill-[#721C28]" /> ~{session.caloriesBurned} kcal
                    </span>
                  </div>

                  <h3 className="font-serif font-black text-lg text-[#231815] group-hover:text-[#721C28] transition-colors">
                    {session.name}
                  </h3>
                  <p className="text-xs text-[#5C4F4D] mt-1 leading-relaxed">
                    {session.benefits}
                  </p>
                </div>

                <div className="bg-[#FAF7F2] p-3 rounded-2xl border border-[#E5DDD0] flex flex-col gap-1 text-[11px] text-[#7A6E6B]">
                  <span className="font-bold text-[#231815]">Duration: {session.durationMinutes} Minutes</span>
                  <span>{session.steps.length} Structured Intervals</span>
                </div>

                <button
                  onClick={() => onStartVideoSession(session)}
                  className="w-full py-3 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white font-black rounded-full text-xs flex items-center justify-center gap-2 cursor-pointer shadow-lg shadow-[#721C28]/15 transition-all uppercase tracking-wider"
                >
                  <Play className="w-4 h-4 fill-white" />
                  <span>START STUDIO STREAM</span>
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

    </div>
  )
}
