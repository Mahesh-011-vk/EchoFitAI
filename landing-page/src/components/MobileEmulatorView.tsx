import React, { useState } from 'react'
import {
  Flame,
  Dumbbell,
  Apple,
  TrendingUp,
  MessageSquare,
  Zap,
  ArrowRight,
  Camera,
  Activity,
  ChevronRight,
  Menu,
  Moon,
  Sparkles
} from 'lucide-react'
import type { UserProfile, GoalAnalysis, SavedRoutine, MealItem, ProgressPhotoItem, CardioSession, GuidedProgram, RecoveryMetrics, CalendarDayItem } from '../types/fitness'
import { MarqueeScroller } from './MarqueeScroller'

interface MobileEmulatorViewProps {
  userProfile: UserProfile
  goalAnalysis: GoalAnalysis
  activeRoutine: SavedRoutine
  mealsLogged: MealItem[]
  waterGlasses: number
  setWaterGlasses: React.Dispatch<React.SetStateAction<number>>
  workoutCompleted: boolean
  setWorkoutCompleted: (completed: boolean) => void
  streakCount: number
  photosList: ProgressPhotoItem[]
  onOpenManualMeal: () => void
  onOpenAccount: () => void
  onStartVideoSession: (session: CardioSession) => void
  recoveryMetrics: RecoveryMetrics
  setRecoveryMetrics: React.Dispatch<React.SetStateAction<RecoveryMetrics>>
  guidedPrograms: GuidedProgram[]
  onStartGuidedProgram: (program: GuidedProgram) => void
  calendarDays: CalendarDayItem[]
  selectedDate: number
  setSelectedDate: (date: number) => void
}

export const MobileEmulatorView: React.FC<MobileEmulatorViewProps> = ({
  userProfile,
  goalAnalysis,
  activeRoutine,
  mealsLogged,
  waterGlasses,
  setWaterGlasses,
  workoutCompleted,
  setWorkoutCompleted,
  streakCount,
  photosList,
  onOpenManualMeal,
  onOpenAccount,
  onStartVideoSession,
  recoveryMetrics,
  setRecoveryMetrics,
  guidedPrograms,
  onStartGuidedProgram,
  calendarDays,
  selectedDate,
  setSelectedDate
}) => {
  const [mobileTab, setMobileTab] = useState<'HOME' | 'WORKOUT' | 'DIET' | 'PROGRESS' | 'ECHOAI'>('HOME')

  const totalCalories = mealsLogged.reduce((sum, m) => sum + m.calories, 0)
  const totalProtein = mealsLogged.reduce((sum, m) => sum + m.protein, 0)
  const remainingCalories = Math.max(0, goalAnalysis.calorieTarget - totalCalories)

  const todaySplit = activeRoutine.splits[0] || {
    name: 'Push Day',
    focus: 'Chest & Triceps',
    exercises: []
  }

  return (
    <div className="w-full max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-12 gap-8 items-center py-4">
      
      {/* Left Column: Product Information and Features */}
      <div className="lg:col-span-7 flex flex-col gap-6 text-left max-w-xl mx-auto lg:mx-0 pr-0 lg:pr-8">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-2xl bg-gradient-to-br from-[#721C28] via-[#8C2434] to-[#4E101B] flex items-center justify-center text-white font-serif font-black text-xl shadow-lg shadow-[#721C28]/25">
            ✦
          </div>
          <div>
            <h2 className="font-serif font-black text-2xl tracking-wider text-[#231815]">ECHOFIT MOBILE</h2>
            <p className="text-[10px] text-[#721C28] font-black tracking-widest uppercase">Pixel-Perfect Android Companion</p>
          </div>
        </div>

        <h1 className="font-serif font-black text-4xl sm:text-5xl text-[#231815] tracking-tight leading-none mt-2">
          Experience EchoFit <br/>
          <span className="bg-gradient-to-r from-[#721C28] via-[#942537] to-[#4E101B] bg-clip-text text-transparent">
            Exact Mobile Experience
          </span>
        </h1>

        <p className="text-[#5C4F4D] text-sm leading-relaxed">
          The exact mobile interface shown in your design reference: live recovery wave graphs, bio-energy radar, date selector with activity dots, and glowing guided protocols.
        </p>

        <div className="grid grid-cols-2 gap-4 mt-1">
          {[
            { title: "Recovery 86% Wave", desc: "Autonomic nervous system readiness tracking" },
            { title: "Bio-Energy Radar", desc: "Live biometric sensor orbit" },
            { title: "Guided Protocols", desc: "Morning Reset, Deep Focus & Recovery" },
            { title: "Calendar Dots Strip", desc: "Multi-colored compliance activity tracker" }
          ].map((item, idx) => (
            <div key={idx} className="p-3 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
              <span className="text-xs font-bold text-[#231815] flex items-center gap-1.5">
                <span className="w-1.5 h-1.5 rounded-full bg-[#721C28]" />
                {item.title}
              </span>
              <span className="text-[10px] text-[#7A6E6B] font-medium">{item.desc}</span>
            </div>
          ))}
        </div>

        <div className="flex flex-wrap items-center gap-4 mt-3">
          <a
            href="https://github.com/Mahesh-011-vk/EchoFitAI/releases"
            target="_blank"
            rel="noreferrer"
            className="flex items-center gap-2 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white px-6 py-3 rounded-full text-xs font-black tracking-wide shadow-xl shadow-[#721C28]/20 cursor-pointer transition-all uppercase"
          >
            DOWNLOAD ANDROID APK
            <ArrowRight className="w-4 h-4" />
          </a>
          <button
            onClick={onOpenAccount}
            className="flex items-center gap-2 border border-[#E5DDD0] bg-white hover:bg-[#FAF7F2] text-[#231815] px-5 py-3 rounded-full text-xs font-bold cursor-pointer transition-all shadow-sm"
          >
            MANAGE STORED ROUTINES
          </button>
        </div>

        <div className="w-full mt-4 border-t border-[#E8E1D5] pt-6 hidden sm:block">
          <MarqueeScroller />
        </div>
      </div>

      {/* Right Column: Smartphone Mockup Shell */}
      <div className="lg:col-span-5 flex justify-center py-4 w-full">
        <div className="w-[375px] h-[812px] bg-[#FAF7F2] border-[12px] border-[#2E2429] rounded-[48px] shadow-[0_30px_100px_rgba(70,45,35,0.22)] relative flex flex-col overflow-hidden select-none">
          
          {/* Dynamic Island Notch */}
          <div className="w-[110px] h-[26px] bg-[#000000] rounded-full absolute top-2 left-1/2 -translate-x-1/2 z-50 flex items-center justify-between px-2.5">
            <span className="w-2.5 h-2.5 rounded-full bg-slate-900 border border-slate-800" />
            <span className="w-2 h-2 rounded-full bg-slate-950" />
          </div>

          {/* Status Bar */}
          <div className="h-[36px] px-6 pt-2 flex justify-between items-center text-[11px] text-[#231815] font-bold select-none z-40 bg-[#FAF7F2]">
            <span>1:47</span>
            <div className="flex items-center gap-1.5 text-[10px]">
              <span>5G</span>
              <span>📶</span>
              <span>🔋</span>
            </div>
          </div>

          {/* Core Scrollable Content */}
          <div className="flex-1 overflow-y-auto px-4 py-3 flex flex-col gap-4 bg-[#F8F5EE] relative z-20 scrollbar-none text-left">
            
            {/* Top Bar with Lotus Logo and Dashboard Title */}
            <div className="flex justify-between items-center px-1">
              <div className="flex items-center gap-2">
                <span className="text-xl">🪷</span>
                <h3 className="font-serif font-black text-xl text-[#231815] tracking-tight">Dashboard</h3>
              </div>
              <button onClick={onOpenAccount} className="text-[#231815] hover:text-[#721C28] cursor-pointer">
                <Menu className="w-5 h-5" />
              </button>
            </div>

            {/* TAB: HOME */}
            {mobileTab === 'HOME' && (
              <div className="flex flex-col gap-4 animate-in fade-in">
                
                {/* Hero Section: Athlete Silhouette + Calendar Dates */}
                <div className="grid grid-cols-12 gap-3 items-center">
                  <div className="col-span-7 h-44 rounded-2xl overflow-hidden border border-[#E5DDD0] relative bg-black shadow-md">
                    <img
                      src="/athlete_hero.jpg"
                      alt="Athlete"
                      className="w-full h-full object-cover filter contrast-125"
                    />
                    <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-transparent" />
                  </div>

                  {/* Calendar Dates Strip */}
                  <div className="col-span-5 grid grid-cols-2 gap-2 text-center">
                    {calendarDays.map(item => {
                      const isSelected = selectedDate === item.date
                      return (
                        <button
                          key={item.date}
                          onClick={() => setSelectedDate(item.date)}
                          className={`p-2 rounded-xl border flex flex-col items-center gap-0.5 transition-all cursor-pointer ${
                            isSelected
                              ? 'liquid-tab-active border-white shadow-md text-[#721C28]'
                              : 'bg-white border-[#E5DDD0] text-[#231815]'
                          }`}
                        >
                          <span className="font-serif font-black text-lg leading-none">
                            {item.date}
                          </span>
                          <span className="text-[9px] uppercase font-bold text-[#7A6E6B]">
                            {item.dayName}
                          </span>
                          <div className="flex items-center gap-0.5 mt-0.5">
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
                </div>

                {/* Floating "Recovery today: 86%" Card */}
                <div className="classic-card rounded-[32px] p-5 flex flex-col gap-4 text-[#231815] shadow-md relative">
                  
                  <div className="flex justify-between items-start">
                    <div>
                      <span className="text-[11px] font-bold text-[#7A6E6B] block">Recovery today</span>
                      <div className="flex items-baseline gap-1 mt-0.5">
                        <span className="font-serif font-black text-4xl text-[#231815]">
                          {recoveryMetrics.score}
                        </span>
                        <span className="text-xl font-black text-[#721C28]">%</span>
                      </div>
                    </div>

                    {/* Concentric Bio-Radar */}
                    <div className="relative w-20 h-20 flex items-center justify-center">
                      <div className="absolute inset-0 rounded-full border border-dashed border-[#DCD4C7] animate-radar-spin" />
                      <div className="absolute inset-2 rounded-full border border-[#E5DDD0]" />
                      <div className="w-5 h-5 rounded-full bg-[#721C28] flex items-center justify-center text-white text-[8px]">
                        <span className="w-2 h-2 rounded-full bg-white" />
                      </div>
                      
                      {/* Floating Energy Lightning Badge in Liquid Glass */}
                      <div className="absolute -top-1 -right-1 p-1 rounded-full liquid-glass-capsule shadow-sm">
                        <Zap className="w-3 h-3 text-[#721C28] fill-[#721C28]" />
                      </div>
                      {/* Floating Activity Badge in Liquid Glass */}
                      <div className="absolute -bottom-1 -left-1 p-1 rounded-full liquid-glass-capsule shadow-sm">
                        <Activity className="w-3 h-3 text-[#721C28]" />
                      </div>
                    </div>
                  </div>

                  {/* Wave Sparkline Graph */}
                  <div className="relative w-full h-12 flex items-end">
                    <svg className="w-full h-full overflow-visible" viewBox="0 0 240 50" preserveAspectRatio="none">
                      <path
                        d="M 0,35 C 50,38 90,28 130,32 C 170,36 200,20 230,12"
                        fill="transparent"
                        stroke="#721C28"
                        strokeWidth="2.5"
                        strokeLinecap="round"
                      />
                      <circle cx="230" cy="12" r="4" fill="#FFFFFF" stroke="#721C28" strokeWidth="2.5" />
                    </svg>
                  </div>

                  <div className="w-full flex justify-center -mb-2">
                    <span className="text-xs text-[#7A6E6B] font-black">^</span>
                  </div>
                </div>

                {/* "Program" Section */}
                <div className="flex flex-col gap-2.5">
                  <div className="flex justify-between items-center">
                    <h4 className="font-serif font-black text-base text-[#231815]">Program</h4>
                    <span className="text-[10px] text-[#721C28] font-bold">Guided Protocols</span>
                  </div>

                  <div className="flex gap-3 overflow-x-auto pb-2 scrollbar-none">
                    {guidedPrograms.map(prog => (
                      <div
                        key={prog.id}
                        onClick={() => onStartGuidedProgram(prog)}
                        className="relative w-36 h-40 rounded-[28px] classic-card p-3 flex flex-col justify-between shrink-0 overflow-hidden cursor-pointer group shadow-sm"
                      >
                        <div className="relative z-10 flex justify-between items-start">
                          <div>
                            <h5 className="font-serif font-black text-xs text-[#231815] leading-tight">
                              {prog.title}
                            </h5>
                            <span className="text-[9px] text-[#7A6E6B] font-bold block mt-0.5">
                              {prog.completedSessions}/{prog.totalSessions}
                            </span>
                          </div>
                          <div className="w-5 h-5 rounded-full bg-[#FAF7F2] border border-[#E5DDD0] flex items-center justify-center text-[#721C28]">
                            <ChevronRight className="w-3 h-3 ml-0.5" />
                          </div>
                        </div>

                        <div className="relative z-10 bg-[#FAF7F2] border border-[#E5DDD0] rounded-xl p-1.5 text-center text-[9px] text-[#231815] font-bold">
                          {prog.durationMinutes} Mins
                        </div>
                      </div>
                    ))}
                  </div>
                </div>

                {/* Quick Missions */}
                <div className="classic-card p-4 rounded-3xl flex flex-col gap-2.5 text-xs shadow-sm">
                  <div className="flex justify-between items-center text-[10px] font-bold text-[#721C28] uppercase">
                    <span>Daily Split</span>
                    <span>{workoutCompleted ? '✓ Done' : 'Pending'}</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-[#231815] font-bold">{todaySplit.name}</span>
                    {!workoutCompleted && (
                      <button
                        onClick={() => setWorkoutCompleted(true)}
                        className="bg-[#721C28] text-white px-3 py-1 rounded-full text-[10px] font-extrabold cursor-pointer shadow-sm"
                      >
                        Mark Done
                      </button>
                    )}
                  </div>
                </div>

              </div>
            )}

            {/* TAB: WORKOUT */}
            {mobileTab === 'WORKOUT' && (
              <div className="flex flex-col gap-3 animate-in fade-in text-xs">
                <div className="flex justify-between items-center">
                  <h4 className="font-serif font-black text-sm text-[#231815]">{activeRoutine.name}</h4>
                  <span className="text-[9px] text-[#721C28] font-bold">{todaySplit.name}</span>
                </div>

                <div className="flex flex-col gap-2">
                  {todaySplit.exercises.map(ex => (
                    <div key={ex.id} className="p-3 classic-card rounded-2xl flex justify-between items-center shadow-sm">
                      <div>
                        <h5 className="font-serif font-bold text-xs text-[#231815]">{ex.name}</h5>
                        <p className="text-[9px] text-[#7A6E6B]">{ex.sets} Sets × {ex.reps} Reps | {ex.targetMuscle}</p>
                      </div>
                      <span className="text-[9px] text-[#721C28] font-bold">{ex.equipment}</span>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* TAB: DIET */}
            {mobileTab === 'DIET' && (
              <div className="flex flex-col gap-3 animate-in fade-in">
                <div className="flex justify-between items-center">
                  <h4 className="font-serif font-black text-sm text-[#231815]">DIET TRACKER</h4>
                  <button
                    onClick={onOpenManualMeal}
                    className="bg-[#721C28] text-white px-3 py-1 rounded-full text-[10px] font-extrabold cursor-pointer shadow-sm"
                  >
                    + Add Food
                  </button>
                </div>

                <div className="grid grid-cols-3 gap-2 text-center">
                  <div className="p-2 classic-card rounded-xl">
                    <span className="text-[8px] text-[#7A6E6B] uppercase block">Calories</span>
                    <span className="text-xs font-black text-[#231815]">{totalCalories}</span>
                  </div>
                  <div className="p-2 classic-card rounded-xl">
                    <span className="text-[8px] text-[#721C28] uppercase block">Protein</span>
                    <span className="text-xs font-black text-[#721C28]">{totalProtein}g</span>
                  </div>
                  <div className="p-2 classic-card rounded-xl">
                    <span className="text-[8px] text-emerald-800 uppercase block">Budget</span>
                    <span className="text-xs font-black text-[#231815]">{userProfile.budgetLevel}</span>
                  </div>
                </div>

                <div className="flex flex-col gap-2">
                  {mealsLogged.map(meal => (
                    <div key={meal.id} className="p-2.5 classic-card rounded-xl flex justify-between items-center text-xs shadow-sm">
                      <div>
                        <p className="font-serif font-bold text-[#231815] text-[11px]">{meal.name}</p>
                        <p className="text-[9px] text-[#7A6E6B]">{meal.portion}</p>
                      </div>
                      <span className="font-bold text-[#721C28] text-[10px]">{meal.calories} kcal</span>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* TAB: PROGRESS */}
            {mobileTab === 'PROGRESS' && (
              <div className="flex flex-col gap-3 animate-in fade-in">
                <h4 className="font-serif font-black text-sm text-[#231815]">PROGRESS & PHOTOS</h4>
                <div className="p-3 classic-card rounded-2xl flex justify-between items-center text-xs shadow-sm">
                  <div>
                    <span className="text-[9px] text-[#7A6E6B] uppercase block">Current Weight</span>
                    <span className="text-base font-serif font-black text-[#231815]">{userProfile.weight} kg</span>
                  </div>
                  <div>
                    <span className="text-[9px] text-[#7A6E6B] uppercase block">Target Goal</span>
                    <span className="text-base font-serif font-black text-[#721C28]">{userProfile.targetWeight} kg</span>
                  </div>
                </div>

                <div className="grid grid-cols-3 gap-2">
                  {['Front', 'Side', 'Back'].map(pos => {
                    const p = photosList.find(item => item.position === pos)
                    return (
                      <div key={pos} className="h-28 rounded-xl bg-white border border-[#E5DDD0] flex flex-col items-center justify-center relative overflow-hidden shadow-sm">
                        {p ? (
                          <img src={p.url} alt={pos} className="w-full h-full object-cover" />
                        ) : (
                          <Camera className="w-4 h-4 text-[#A59995]" />
                        )}
                        <span className="absolute bottom-1 text-[8px] bg-white/90 border border-[#E5DDD0] px-1.5 py-0.5 rounded text-[#231815] font-bold">{pos}</span>
                      </div>
                    )
                  })}
                </div>
              </div>
            )}

            {/* TAB: ECHOAI */}
            {mobileTab === 'ECHOAI' && (
              <div className="flex flex-col gap-3 animate-in fade-in">
                <h4 className="font-serif font-black text-sm text-[#231815]">COACH ECHO</h4>
                <div className="p-4 bg-[#FAF7F2] border border-[#721C28]/25 rounded-2xl text-xs text-[#231815] shadow-sm leading-relaxed">
                  "Keep going, {userProfile.name}! Readiness is at {recoveryMetrics.score}%. Target your sets with full intensity today!"
                </div>
              </div>
            )}

          </div>

          {/* Bottom Streamlined Navigation Bar - LIQUID FROSTED GLASS CAPSULE (MATCHING ATTACHED IMAGE) */}
          <div className="h-[68px] border-t border-[#E8E1D5] bg-[#FAF7F2] px-3 flex items-center justify-center shrink-0 z-40 relative">
            <div className="w-full liquid-glass-capsule p-1 flex justify-around items-center shadow-md">
              {[
                { id: 'HOME', label: 'Home', icon: Zap },
                { id: 'WORKOUT', label: 'Workout', icon: Dumbbell },
                { id: 'DIET', label: 'Diet', icon: Apple },
                { id: 'PROGRESS', label: 'Progress', icon: TrendingUp },
                { id: 'ECHOAI', label: 'Echo AI', icon: MessageSquare }
              ].map(tab => {
                const Icon = tab.icon
                const isActive = mobileTab === tab.id
                return (
                  <button
                    key={tab.id}
                    onClick={() => setMobileTab(tab.id as any)}
                    className={`relative z-10 flex flex-col items-center gap-0.5 py-1 px-2.5 rounded-full cursor-pointer transition-all ${
                      isActive
                        ? 'liquid-tab-active text-[#721C28] font-black'
                        : 'liquid-tab-idle'
                    }`}
                  >
                    <Icon className={`w-3.5 h-3.5 ${isActive ? 'text-[#721C28]' : 'text-[#6A5D60]'}`} />
                    <span className="text-[7.5px] font-black tracking-wider uppercase">{tab.label}</span>
                  </button>
                )
              })}
            </div>
          </div>

        </div>
      </div>

    </div>
  )
}
