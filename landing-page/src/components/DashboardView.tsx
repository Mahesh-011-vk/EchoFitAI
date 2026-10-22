import React, { useState } from 'react'
import {
  Droplet,
  Dumbbell,
  Apple,
  CheckCircle2,
  ArrowRight,
  TrendingUp,
  Award,
  Plus,
  RotateCcw,
  Sparkles,
  Zap,
  Activity,
  Heart,
  Moon,
  ChevronRight,
  Search,
  Bell,
  Sliders,
  Wind
} from 'lucide-react'
import type { UserProfile, GoalAnalysis, MealItem, SavedRoutine, GuidedProgram, RecoveryMetrics, CalendarDayItem } from '../types/fitness'

interface DashboardViewProps {
  userProfile: UserProfile
  goalAnalysis: GoalAnalysis
  mealsLogged: MealItem[]
  waterGlasses: number
  setWaterGlasses: React.Dispatch<React.SetStateAction<number>>
  workoutCompleted: boolean
  setWorkoutCompleted: (completed: boolean) => void
  activeRoutine: SavedRoutine
  streakCount: number
  onNavigate: (tab: string) => void
  onOpenManualMeal: () => void
  recoveryMetrics: RecoveryMetrics
  setRecoveryMetrics: React.Dispatch<React.SetStateAction<RecoveryMetrics>>
  guidedPrograms: GuidedProgram[]
  onStartGuidedProgram: (program: GuidedProgram) => void
  calendarDays: CalendarDayItem[]
  selectedDate: number
  setSelectedDate: (date: number) => void
}

export const DashboardView: React.FC<DashboardViewProps> = ({
  userProfile,
  goalAnalysis,
  mealsLogged,
  waterGlasses,
  setWaterGlasses,
  workoutCompleted,
  setWorkoutCompleted,
  activeRoutine,
  streakCount,
  onNavigate,
  onOpenManualMeal,
  recoveryMetrics,
  setRecoveryMetrics,
  guidedPrograms,
  onStartGuidedProgram,
  calendarDays,
  selectedDate,
  setSelectedDate
}) => {
  // Timeframe filter (Today vs Week)
  const [timeframe, setTimeframe] = useState<'Today' | 'Week'>('Today')
  const [notificationsEnabled, setNotificationsEnabled] = useState(true)
  const [searchQuery, setSearchQuery] = useState('')

  // Aggregate nutrition metrics
  const totalCalories = mealsLogged.reduce((sum, m) => sum + m.calories, 0)
  const totalProtein = mealsLogged.reduce((sum, m) => sum + m.protein, 0)
  const totalCarbs = mealsLogged.reduce((sum, m) => sum + m.carbs, 0)
  const totalFats = mealsLogged.reduce((sum, m) => sum + m.fats, 0)

  const remainingCalories = Math.max(0, goalAnalysis.calorieTarget - totalCalories)
  const caloriePercent = Math.min(100, Math.round((totalCalories / goalAnalysis.calorieTarget) * 100))
  const proteinPercent = Math.min(100, Math.round((totalProtein / goalAnalysis.proteinTarget) * 100))
  const carbsPercent = Math.min(100, Math.round((totalCarbs / goalAnalysis.carbsTarget) * 100))
  const fatsPercent = Math.min(100, Math.round((totalFats / goalAnalysis.fatsTarget) * 100))

  const targetWaterGlasses = Math.round(goalAnalysis.waterIntakeTargetLiters * 4)

  // Today's active split
  const todaySplit = activeRoutine.splits[0] || {
    name: 'Push Day',
    focus: 'Chest, Shoulders & Triceps',
    exercises: []
  }

  // Adjust Sleep / Recalculate Recovery Score
  const handleAdjustSleep = (delta: number) => {
    const newSleep = Math.max(4, Math.min(12, Math.round((recoveryMetrics.sleepHours + delta) * 10) / 10))
    const sleepFactor = (newSleep / 8) * 40
    const hydrationBonus = Math.min(15, waterGlasses * 1.5)
    const workoutBonus = workoutCompleted ? 20 : 15
    const baseHRV = (recoveryMetrics.hrvMs / 80) * 25
    const newScore = Math.min(99, Math.max(45, Math.round(sleepFactor + hydrationBonus + workoutBonus + baseHRV)))

    setRecoveryMetrics({
      ...recoveryMetrics,
      sleepHours: newSleep,
      score: newScore,
      status: newScore >= 80 ? 'Optimal Readiness' : newScore >= 65 ? 'Moderate Readiness' : 'Rest Advised'
    })
  }

  // Filter guided programs if user searches
  const filteredPrograms = guidedPrograms.filter(p =>
    p.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
    p.subtitle.toLowerCase().includes(searchQuery.toLowerCase())
  )

  return (
    <div className="flex flex-col gap-8 w-full max-w-7xl mx-auto pb-16 animate-in fade-in duration-300">
      
      {/* ========================================================================= */}
      {/* 1. HERO SECTION: CLASSIC ATHLETE MATTE + LIQUID GLASS TABS               */}
      {/* ========================================================================= */}
      <div className="relative rounded-[36px] bg-white border border-[#E5DDD0] overflow-hidden p-6 sm:p-8 shadow-[0_15px_45px_rgba(65,45,35,0.06)]">
        
        {/* Ambient background glows */}
        <div className="absolute top-0 right-1/4 w-96 h-96 bg-[#EBDDC8]/40 rounded-full blur-[130px] pointer-events-none" />
        <div className="absolute bottom-0 left-10 w-80 h-80 bg-[#721C28]/5 rounded-full blur-[140px] pointer-events-none" />

        <div className="relative z-10 grid grid-cols-1 lg:grid-cols-12 gap-8 items-center">
          
          {/* Left Column: Monochromatic Athlete Silhouette & Brand Title */}
          <div className="lg:col-span-7 flex flex-col sm:flex-row items-center sm:items-start gap-6">
            <div className="relative w-44 h-56 sm:w-52 sm:h-64 rounded-3xl overflow-hidden shrink-0 border border-[#E5DDD0] shadow-xl bg-black">
              <img
                src="/athlete_hero.jpg"
                alt="EchoFit Athlete Silhouette"
                className="w-full h-full object-cover object-center filter contrast-125"
              />
              <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-transparent" />
              <div className="absolute bottom-3 left-3 flex items-center gap-1.5 bg-black/70 backdrop-blur-md px-2.5 py-1 rounded-full border border-white/20">
                <span className="w-2 h-2 rounded-full bg-[#E54860] animate-ping" />
                <span className="text-[9px] font-black tracking-widest uppercase text-white">Live Biomarkers</span>
              </div>
            </div>

            <div className="flex flex-col gap-3 text-left">
              <div className="flex items-center gap-2">
                <span className="text-xl">🪷</span>
                <span className="font-serif font-black text-2xl sm:text-3xl tracking-tight text-[#231815]">
                  Dashboard
                </span>
              </div>
              <p className="text-[#5C4F4D] text-xs sm:text-sm font-medium leading-relaxed max-w-md">
                Welcome, <span className="text-[#721C28] font-bold">{userProfile.name}</span>. Your autonomic readiness is at an all-time peak today. Ready for {activeRoutine.name}?
              </p>

              {/* LIQUID FROSTED GLASS TABS (MATCHING USER'S ATTACHED IMAGE) */}
              <div className="flex items-center gap-3 mt-1">
                <div className="inline-flex p-1 rounded-full liquid-glass-capsule shadow-md">
                  <button
                    onClick={() => setTimeframe('Today')}
                    className={`px-5 py-1.5 rounded-full text-xs transition-all cursor-pointer ${
                      timeframe === 'Today'
                        ? 'liquid-tab-active shadow-md text-[#721C28] font-black'
                        : 'liquid-tab-idle font-semibold'
                    }`}
                  >
                    Today
                  </button>
                  <button
                    onClick={() => setTimeframe('Week')}
                    className={`px-5 py-1.5 rounded-full text-xs transition-all cursor-pointer ${
                      timeframe === 'Week'
                        ? 'liquid-tab-active shadow-md text-[#721C28] font-black'
                        : 'liquid-tab-idle font-semibold'
                    }`}
                  >
                    Week
                  </button>
                </div>

                {/* Notifications Toggle */}
                <div className="hidden sm:flex items-center gap-2 bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-1.5 rounded-full text-xs text-[#5C4F4D]">
                  <span className="text-[11px] font-bold">Notifications</span>
                  <button
                    onClick={() => setNotificationsEnabled(!notificationsEnabled)}
                    className={`w-10 h-5 rounded-full transition-colors relative cursor-pointer ${
                      notificationsEnabled ? 'bg-[#721C28]' : 'bg-[#DCD4C7]'
                    }`}
                  >
                    <span className={`w-4 h-4 rounded-full bg-white absolute top-0.5 transition-transform shadow-md ${
                      notificationsEnabled ? 'left-5.5' : 'left-0.5'
                    }`} />
                  </button>
                </div>
              </div>
            </div>
          </div>

          {/* Right Column: Calendar Date Strip with multi-colored dots */}
          <div className="lg:col-span-5 flex flex-col gap-3">
            <div className="flex justify-between items-center text-xs font-bold text-[#7A6E6B] uppercase tracking-wider">
              <span>Weekly Cycle</span>
              <span className="text-[#721C28] font-black">Streak: {streakCount} Days Active</span>
            </div>

            <div className="grid grid-cols-4 gap-2.5">
              {calendarDays.map(item => {
                const isSelected = selectedDate === item.date
                return (
                  <button
                    key={item.date}
                    onClick={() => setSelectedDate(item.date)}
                    className={`p-3 rounded-2xl border transition-all cursor-pointer flex flex-col items-center gap-1.5 relative ${
                      isSelected
                        ? 'liquid-tab-active border-white/95 shadow-md text-[#721C28]'
                        : 'bg-[#FAF7F2] hover:bg-white border-[#E5DDD0] text-[#3D3130]'
                    }`}
                  >
                    <span className="font-serif font-black text-2xl">
                      {item.date}
                    </span>
                    <span className="text-[10px] uppercase font-bold text-[#7A6E6B] tracking-wider">
                      {item.dayName}
                    </span>
                    {/* Activity Dots */}
                    <div className="flex items-center gap-1 mt-0.5">
                      {item.dots.map((dotColor, dotIdx) => (
                        <span
                          key={dotIdx}
                          className="w-1.5 h-1.5 rounded-full"
                          style={{ backgroundColor: dotColor }}
                        />
                      ))}
                    </div>
                  </button>
                )
              })}
            </div>

            {/* Classic Luxury Search Bar */}
            <div className="relative mt-1">
              <Search className="w-4 h-4 text-[#8A7D79] absolute left-3.5 top-1/2 -translate-y-1/2 pointer-events-none" />
              <input
                type="text"
                placeholder="Search programs, exercises, or recovery protocols..."
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
                className="w-full bg-[#FAF7F2] hover:bg-white border border-[#E2D8C9] rounded-full py-2 pl-10 pr-4 text-xs text-[#231815] placeholder-[#8A7D79] focus:outline-none focus:border-[#721C28] transition-all shadow-inner"
              />
            </div>
          </div>

        </div>

      </div>

      {/* ========================================================================= */}
      {/* 2. RECOVERY TODAY 86% FLOATING CARD & RADAR (CLASSIC AESTHETIC)            */}
      {/* ========================================================================= */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-stretch">
        
        {/* Recovery Score Card (lg:col-span-7) */}
        <div className="lg:col-span-7 classic-card rounded-[36px] p-6 sm:p-8 flex flex-col justify-between gap-6 relative overflow-hidden group">
          
          <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 border-b border-[#E8E1D5] pb-4">
            <div>
              <span className="text-xs font-black uppercase tracking-wider text-[#7A6E6B]">
                Readiness & Recovery
              </span>
              <div className="flex items-baseline gap-2 mt-1">
                <span className="font-serif font-black text-4xl sm:text-5xl text-[#231815]">
                  {recoveryMetrics.score}
                </span>
                <span className="text-2xl font-black text-[#721C28]">%</span>
                <span className="text-xs font-bold text-[#721C28] bg-[#721C28]/10 border border-[#721C28]/25 px-3 py-1 rounded-full ml-2">
                  {recoveryMetrics.status}
                </span>
              </div>
            </div>

            {/* Interactive Sleep Adjuster */}
            <div className="flex items-center gap-2 bg-[#FAF7F2] border border-[#E5DDD0] p-1.5 rounded-2xl text-xs">
              <div className="flex items-center gap-1.5 px-2 text-[#5C4F4D] font-bold">
                <Moon className="w-3.5 h-3.5 text-[#721C28]" />
                <span>{recoveryMetrics.sleepHours}h Sleep</span>
              </div>
              <div className="flex items-center gap-1">
                <button
                  onClick={() => handleAdjustSleep(-0.5)}
                  className="w-6 h-6 rounded-lg bg-white border border-[#E5DDD0] hover:bg-[#F3EFE6] text-[#231815] font-black cursor-pointer shadow-sm"
                  title="Decrease sleep hours"
                >
                  -
                </button>
                <button
                  onClick={() => handleAdjustSleep(0.5)}
                  className="w-6 h-6 rounded-lg bg-[#721C28] hover:bg-[#58121D] text-white font-black cursor-pointer shadow-sm"
                  title="Increase sleep hours"
                >
                  +
                </button>
              </div>
            </div>
          </div>

          {/* Interactive Wave Sparkline Graph with Burgundy Accent */}
          <div className="flex flex-col gap-2">
            <div className="flex justify-between items-center text-xs font-bold text-[#7A6E6B]">
              <span>Autonomic Readiness Trend</span>
              <span className="text-[#721C28] font-mono font-bold">HRV: {recoveryMetrics.hrvMs} ms ● Soreness: {recoveryMetrics.sorenessLevel}</span>
            </div>

            <div className="relative w-full h-24 flex items-end">
              <svg className="w-full h-full overflow-visible" viewBox="0 0 400 80" preserveAspectRatio="none">
                <defs>
                  <linearGradient id="waveGradientBurgundy" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="#721C28" stopOpacity="0.25" />
                    <stop offset="100%" stopColor="#721C28" stopOpacity="0.0" />
                  </linearGradient>
                </defs>
                {/* Wave Area Fill */}
                <path
                  d="M 0,60 C 80,62 140,50 200,55 C 260,60 320,35 380,25 L 380,80 L 0,80 Z"
                  fill="url(#waveGradientBurgundy)"
                />
                {/* Wave Stroke Line */}
                <path
                  d="M 0,60 C 80,62 140,50 200,55 C 260,60 320,35 380,25"
                  fill="transparent"
                  stroke="#721C28"
                  strokeWidth="3.5"
                  strokeLinecap="round"
                />
                {/* Endpoint Node */}
                <circle cx="380" cy="25" r="5" fill="#FFFFFF" stroke="#721C28" strokeWidth="3" />
              </svg>
            </div>
          </div>

          {/* Recommendations Strip */}
          <div className="p-4 rounded-2xl bg-[#FAF7F2] border border-[#E5DDD0] flex items-center justify-between text-xs text-[#5C4F4D]">
            <div className="flex items-center gap-2.5">
              <Sparkles className="w-4 h-4 text-[#721C28] shrink-0" />
              <span>
                <strong>Coach Directive:</strong> 86% recovery indicates peak neural activation. Excellent day to attempt progressive overload on compound lifts.
              </span>
            </div>
            <button
              onClick={() => onNavigate('WORKOUTS')}
              className="text-[#721C28] hover:text-[#50111A] font-extrabold whitespace-nowrap ml-3 cursor-pointer"
            >
              Start Split →
            </button>
          </div>

        </div>

        {/* Target Radar & Energy Rings (lg:col-span-5) */}
        <div className="lg:col-span-5 classic-card rounded-[36px] p-6 sm:p-8 flex flex-col justify-between items-center text-center relative overflow-hidden">
          
          <div className="w-full flex justify-between items-center text-xs font-bold text-[#7A6E6B]">
            <span className="uppercase tracking-wider">Bio-Energy Radar</span>
            <span className="text-[#721C28] font-bold">Active Sensors</span>
          </div>

          {/* Concentric Radar Rings & Floating Badges with Liquid Frosted Capsule */}
          <div className="relative w-48 h-48 my-4 flex items-center justify-center">
            {/* Outer dotted orbit */}
            <div className="absolute inset-0 rounded-full border border-dashed border-[#DCD4C7] animate-radar-spin" />
            {/* Middle ring */}
            <div className="absolute inset-4 rounded-full border border-[#E5DDD0]" />
            {/* Inner ring */}
            <div className="absolute inset-10 rounded-full border border-[#721C28]/25" />
            {/* Center target circle */}
            <div className="w-10 h-10 rounded-full bg-white border border-[#E5DDD0] flex items-center justify-center text-black font-black text-xs shadow-lg shadow-[#721C28]/15">
              <span className="w-3 h-3 rounded-full bg-[#721C28]" />
            </div>

            {/* Floating Energy Lightning Badge in Liquid Glass */}
            <div className="absolute top-2 right-4 p-2.5 rounded-full liquid-glass-capsule shadow-md flex items-center justify-center">
              <Zap className="w-4 h-4 text-[#721C28] fill-[#721C28]" />
            </div>

            {/* Floating Activity Status Badge in Liquid Glass */}
            <div className="absolute bottom-2 left-4 p-2.5 rounded-full liquid-glass-capsule shadow-md flex items-center justify-center">
              <Activity className="w-4 h-4 text-[#721C28]" />
            </div>
          </div>

          {/* Quick Stats Footnote */}
          <div className="w-full grid grid-cols-3 gap-2 text-center text-xs border-t border-[#E8E1D5] pt-4">
            <div>
              <span className="text-[10px] text-[#7A6E6B] block">Vagal Tone</span>
              <span className="font-extrabold text-[#231815]">92%</span>
            </div>
            <div>
              <span className="text-[10px] text-[#7A6E6B] block">Metabolic Burn</span>
              <span className="font-extrabold text-[#721C28]">2,480 kcal</span>
            </div>
            <div>
              <span className="text-[10px] text-[#7A6E6B] block">Stress Level</span>
              <span className="font-extrabold text-[#721C28]">Low</span>
            </div>
          </div>

        </div>

      </div>

      {/* ========================================================================= */}
      {/* 3. "PROGRAM" SECTION: CLASSIC GUIDED PROTOCOLS                           */}
      {/* ========================================================================= */}
      <div className="flex flex-col gap-4">
        
        <div className="flex justify-between items-center">
          <div className="flex items-center gap-2">
            <h2 className="font-serif font-black text-2xl text-[#231815] tracking-tight">
              Program
            </h2>
            <span className="text-[10px] font-black uppercase text-[#721C28] bg-[#721C28]/10 border border-[#721C28]/25 px-2.5 py-0.5 rounded-full">
              Guided Protocols
            </span>
          </div>

          <span className="text-xs text-[#7A6E6B] font-semibold hidden sm:inline">
            Tap any program to launch interactive session
          </span>
        </div>

        {/* Horizontal Grid of Classic Luxury Protocol Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
          {filteredPrograms.map(program => (
            <div
              key={program.id}
              onClick={() => onStartGuidedProgram(program)}
              className="group relative rounded-[32px] classic-card p-5 flex flex-col justify-between gap-6 cursor-pointer overflow-hidden transition-all duration-300 hover:scale-[1.02] hover:border-[#721C28]/40 shadow-sm hover:shadow-md"
            >
              {/* Soft Ambient Warm Glow on Hover */}
              <div 
                className="absolute inset-x-4 bottom-2 h-36 rounded-full opacity-35 group-hover:opacity-75 transition-all duration-700 pointer-events-none filter blur-xl"
                style={{
                  background: `radial-gradient(circle, ${program.auraColor || '#721C28'} 0%, rgba(114, 28, 40, 0.25) 45%, transparent 75%)`
                }}
              />

              {/* Card Header: Program Title & Session Count */}
              <div className="relative z-10 flex justify-between items-start">
                <div>
                  <h3 className="font-serif font-black text-lg text-[#231815] group-hover:text-[#721C28] transition-colors">
                    {program.title}
                  </h3>
                  <p className="text-xs text-[#7A6E6B] font-semibold mt-0.5">
                    {program.completedSessions} / {program.totalSessions} Sessions
                  </p>
                </div>

                <div className="w-8 h-8 rounded-full bg-[#FAF7F2] border border-[#E5DDD0] group-hover:bg-[#721C28] group-hover:text-white flex items-center justify-center text-[#5C4F4D] transition-all shadow-sm">
                  <ChevronRight className="w-4 h-4 ml-0.5" />
                </div>
              </div>

              {/* Card Footer: Duration & Protocol summary */}
              <div className="relative z-10 bg-[#FAF7F2] rounded-2xl p-3 border border-[#E5DDD0] flex justify-between items-center text-xs">
                <span className="font-bold text-[#5C4F4D]">{program.durationMinutes} Mins Duration</span>
                <span className="text-[10px] font-extrabold text-[#721C28] uppercase tracking-wider">Start →</span>
              </div>
            </div>
          ))}
        </div>

      </div>

      {/* ========================================================================= */}
      {/* 4. FITNESS ENERGY & CHECKLIST DASHBOARD (Calories, Macros & Missions)     */}
      {/* ========================================================================= */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6 items-stretch">
        
        {/* Caloric & Macro Gauge Ring (lg:col-span-5) */}
        <div className="lg:col-span-5 classic-card rounded-[36px] p-6 sm:p-8 flex flex-col justify-between items-center gap-6">
          <div className="w-full flex justify-between items-center">
            <div className="flex items-center gap-2">
              <Apple className="w-4 h-4 text-[#721C28]" />
              <h3 className="text-xs font-black uppercase tracking-wider text-[#231815]">Daily Calorie Target</h3>
            </div>
            <button
              onClick={onOpenManualMeal}
              className="text-[10px] bg-[#721C28]/10 hover:bg-[#721C28]/20 text-[#721C28] border border-[#721C28]/25 px-3 py-1.5 rounded-full font-bold cursor-pointer transition-all flex items-center gap-1 shadow-sm"
            >
              <Plus className="w-3 h-3" /> Log Food
            </button>
          </div>

          {/* Circular Progress Gauge */}
          <div className="relative w-44 h-44 flex items-center justify-center">
            <svg className="w-full h-full transform -rotate-90" viewBox="0 0 120 120">
              <circle cx="60" cy="60" r="48" stroke="currentColor" strokeWidth="10" fill="transparent" className="text-[#EFE9DF]" />
              <circle
                cx="60" cy="60" r="48"
                stroke="url(#calorieGradientBurgundy)"
                strokeWidth="10"
                strokeDasharray={2 * Math.PI * 48}
                strokeDashoffset={2 * Math.PI * 48 * (1 - caloriePercent / 100)}
                strokeLinecap="round"
                fill="transparent"
                className="transition-all duration-1000 ease-out"
              />
              <defs>
                <linearGradient id="calorieGradientBurgundy" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" stopColor="#721C28" />
                  <stop offset="100%" stopColor="#A83246" />
                </linearGradient>
              </defs>
            </svg>

            <div className="absolute inset-0 flex flex-col items-center justify-center text-center">
              <span className="font-serif font-black text-3xl text-[#231815] tracking-tight">{remainingCalories}</span>
              <span className="text-[10px] font-bold text-[#7A6E6B] uppercase tracking-widest mt-0.5">kcal left</span>
              <div className="flex items-center gap-1 mt-1 text-[11px] font-extrabold text-[#721C28]">
                <span>{totalCalories}</span>
                <span className="text-[#A59995]">/</span>
                <span className="text-[#5C4F4D]">{goalAnalysis.calorieTarget} kcal</span>
              </div>
            </div>
          </div>

          {/* Macro Bars */}
          <div className="w-full flex flex-col gap-3">
            <div className="flex flex-col gap-1">
              <div className="flex justify-between text-xs font-bold">
                <span className="text-[#3D3130]">Protein: {totalProtein}g / {goalAnalysis.proteinTarget}g</span>
                <span className="text-[#721C28] font-extrabold">{proteinPercent}%</span>
              </div>
              <div className="w-full h-2 bg-[#EFE9DF] rounded-full overflow-hidden">
                <div className="h-full bg-[#721C28] rounded-full" style={{ width: `${proteinPercent}%` }} />
              </div>
            </div>

            <div className="flex flex-col gap-1">
              <div className="flex justify-between text-xs font-bold">
                <span className="text-[#3D3130]">Carbs: {totalCarbs}g / {goalAnalysis.carbsTarget}g</span>
                <span className="text-[#0D9488] font-extrabold">{carbsPercent}%</span>
              </div>
              <div className="w-full h-2 bg-[#EFE9DF] rounded-full overflow-hidden">
                <div className="h-full bg-[#0D9488] rounded-full" style={{ width: `${carbsPercent}%` }} />
              </div>
            </div>
          </div>
        </div>

        {/* Daily Mission Checklist & Hydration (lg:col-span-7) */}
        <div className="lg:col-span-7 classic-card rounded-[36px] p-6 sm:p-8 flex flex-col justify-between gap-5">
          <div className="flex justify-between items-center">
            <h3 className="text-xs font-black uppercase tracking-wider text-[#721C28] flex items-center gap-2">
              <CheckCircle2 className="w-4 h-4 text-[#721C28]" /> Today's Discipline Checklist
            </h3>
            <span className="text-[10px] font-extrabold text-[#7A6E6B] bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-1 rounded-full">
              Mission Status
            </span>
          </div>

          <div className="flex flex-col gap-3">
            {/* Workout Split Check */}
            <div className="p-3.5 bg-[#FAF7F2] border border-[#E5DDD0] rounded-2xl flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className={`w-8 h-8 rounded-xl flex items-center justify-center font-bold text-xs ${
                  workoutCompleted ? 'bg-emerald-500/15 text-emerald-700' : 'bg-white border border-[#E5DDD0] text-[#7A6E6B]'
                }`}>
                  <Dumbbell className="w-4 h-4" />
                </div>
                <div>
                  <h4 className="font-bold text-xs sm:text-sm text-[#231815]">Execute Workout Split</h4>
                  <p className="text-[11px] text-[#7A6E6B]">{todaySplit.name}</p>
                </div>
              </div>
              {workoutCompleted ? (
                <span className="bg-emerald-500/15 text-emerald-800 border border-emerald-500/25 px-3 py-1 rounded-xl text-xs font-bold">
                  ✓ Completed
                </span>
              ) : (
                <button
                  onClick={() => setWorkoutCompleted(true)}
                  className="bg-[#721C28] hover:bg-[#50111A] text-white px-3.5 py-1.5 rounded-full text-xs font-bold cursor-pointer transition-all shadow-sm"
                >
                  Mark Done
                </button>
              )}
            </div>

            {/* Hydration Tracker */}
            <div className="p-3.5 bg-[#FAF7F2] border border-[#E5DDD0] rounded-2xl flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 rounded-xl bg-cyan-500/15 text-cyan-800 flex items-center justify-center">
                  <Droplet className="w-4 h-4" />
                </div>
                <div>
                  <h4 className="font-bold text-xs sm:text-sm text-[#231815]">Hydration Target</h4>
                  <p className="text-[11px] text-[#7A6E6B]">{waterGlasses} / {targetWaterGlasses} glasses (~{(waterGlasses * 0.25).toFixed(1)}L)</p>
                </div>
              </div>
              <div className="flex items-center gap-1.5">
                <button
                  onClick={() => setWaterGlasses(prev => prev + 1)}
                  className="bg-cyan-700 hover:bg-cyan-800 text-white px-3 py-1 rounded-full text-xs font-bold cursor-pointer shadow-sm"
                >
                  + Glass
                </button>
                {waterGlasses > 0 && (
                  <button
                    onClick={() => setWaterGlasses(0)}
                    className="p-1 text-[#8A7D79] hover:text-[#231815]"
                    title="Reset Hydration"
                  >
                    <RotateCcw className="w-3.5 h-3.5" />
                  </button>
                )}
              </div>
            </div>

            {/* Protein Goal */}
            <div className="p-3.5 bg-[#FAF7F2] border border-[#E5DDD0] rounded-2xl flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-8 h-8 rounded-xl bg-[#721C28]/10 text-[#721C28] flex items-center justify-center">
                  <Apple className="w-4 h-4" />
                </div>
                <div>
                  <h4 className="font-bold text-xs sm:text-sm text-[#231815]">Hit Daily Protein Goal</h4>
                  <p className="text-[11px] text-[#7A6E6B]">{totalProtein}g / {goalAnalysis.proteinTarget}g ({proteinPercent}%)</p>
                </div>
              </div>
              <button
                onClick={() => onNavigate('DIET')}
                className="text-xs font-bold text-[#721C28] hover:text-[#50111A]"
              >
                Log Meal →
              </button>
            </div>
          </div>

          {/* Quick Hub Launchers */}
          <div className="grid grid-cols-2 gap-3 pt-2 border-t border-[#E8E1D5]">
            <button
              onClick={() => onNavigate('WORKOUTS')}
              className="py-3 px-4 rounded-2xl bg-[#FAF7F2] hover:bg-white border border-[#E5DDD0] text-xs font-bold text-[#3D3130] hover:text-[#721C28] flex items-center justify-center gap-2 cursor-pointer transition-all shadow-sm"
            >
              <Dumbbell className="w-4 h-4 text-[#721C28]" />
              <span>Full Workout Suite</span>
            </button>
            <button
              onClick={() => onNavigate('ACCOUNT')}
              className="py-3 px-4 rounded-2xl bg-[#FAF7F2] hover:bg-white border border-[#E5DDD0] text-xs font-bold text-[#3D3130] hover:text-[#721C28] flex items-center justify-center gap-2 cursor-pointer transition-all shadow-sm"
            >
              <Award className="w-4 h-4 text-[#721C28]" />
              <span>Stored Routines Hub</span>
            </button>
          </div>
        </div>

      </div>

    </div>
  )
}
