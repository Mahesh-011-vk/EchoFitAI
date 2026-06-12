import { useState } from 'react'
import { motion, AnimatePresence } from 'motion/react'
import {
  ChevronRight,
  Award,
  Plus,
  Trash2,
  Camera,
  Flame,
  Droplet,
  Dumbbell,
  Apple,
  TrendingUp,
  MessageSquare,
  User,
  Send,
  Zap,
  Play,
  Search,
  Calendar,
  Lock,
  Compass,
  Briefcase,
  Share2,
  FolderMinus,
  Settings
} from 'lucide-react'
import { MarqueeScroller } from './components/MarqueeScroller'

interface Meal {
  id: string
  name: string
  portion: string
  calories: number
  protein: number
  carbs: number
  fats: number
}

function App() {
  const [activeTab, setActiveTab] = useState('Dashboard')

  // Shared state for interactive elements
  const [waterGlasses, setWaterGlasses] = useState(6)
  const [waterTarget] = useState(12)
  const [workoutCompleted, setWorkoutCompleted] = useState(false)
  const [mealsLogged, setMealsLogged] = useState<Meal[]>([
    { id: '1', name: 'Scrambled Eggs with Avocado', portion: '1 Plate', calories: 380, protein: 22, carbs: 6, fats: 28 }
  ])
  const streakCount = 5
  const [manualRestDay, setManualRestDay] = useState(false)
  const [cheatMealLogged, setCheatMealLogged] = useState(false)
  const [cheatMealCals, setCheatMealCals] = useState('')
  const [isEnteringCheatMeal, setIsEnteringCheatMeal] = useState(false)

  // Diet Screen States
  const [manualFoodName, setManualFoodName] = useState('')
  const [manualCals, setManualCals] = useState('')
  const [manualPro, setManualPro] = useState('')
  const [isAnalyzingImage, setIsAnalyzingImage] = useState(false)
  const [dietGoal] = useState({ calories: 2200, protein: 140, carbs: 250, fats: 75 })

  // Workout Screen States
  const [activeVideoSession, setActiveVideoSession] = useState<any>(null)
  const [videoTimer, setVideoTimer] = useState(0)
  const [videoIsPlaying, setVideoIsPlaying] = useState(false)

  // Chat Screen States
  const [chatMessages, setChatMessages] = useState<any[]>([
    { sender: 'ai', text: "Hey champion! I'm Coach Echo, your AI fitness guide. Ask me anything about your current workout program, nutrition budgets, or active recovery splits!" }
  ])
  const [userInput, setUserInput] = useState('')
  const [isTyping, setIsTyping] = useState(false)

  // Profile States
  const [userName, setUserName] = useState('mahi')
  const [fitnessGoal, setFitnessGoal] = useState('Muscle Gain')
  const [preferredRestDay, setPreferredRestDay] = useState('Sunday')
  const [dietType, setDietType] = useState('Vegetarian')

  // Progress States
  const [progressWeight, setProgressWeight] = useState('74.5')
  const [weightLogs, setWeightLogs] = useState<{ date: string; weight: number }[]>([
    { date: 'May 20', weight: 75.8 },
    { date: 'May 24', weight: 75.2 },
    { date: 'May 28', weight: 74.9 },
    { date: 'Jun 01', weight: 74.5 }
  ])
  const [newWeightInput, setNewWeightInput] = useState('')
  const progressPhotos = [
    'https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=400&q=80',
    'https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=400&q=80'
  ]

  const cubicBezierEase = [0.16, 1, 0.3, 1] as [number, number, number, number]

  const containerVariants = {
    hidden: { opacity: 0, y: 15 },
    visible: { opacity: 1, y: 0, transition: { duration: 0.5, ease: cubicBezierEase } }
  }

  const handleLogMeal = (name: string, portion: string, cals: number, pro: number, carbs = 30, fats = 10) => {
    const newMeal: Meal = {
      id: Date.now().toString(),
      name,
      portion,
      calories: cals,
      protein: pro,
      carbs,
      fats
    }
    setMealsLogged([...mealsLogged, newMeal])
  }

  const handleManualMealSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!manualFoodName) return
    handleLogMeal(
      manualFoodName,
      '1 serving',
      parseInt(manualCals) || 350,
      parseInt(manualPro) || 20
    )
    setManualFoodName('')
    setManualCals('')
    setManualPro('')
  }

  const triggerCameraScan = () => {
    setIsAnalyzingImage(true)
    setTimeout(() => {
      setIsAnalyzingImage(false)
      handleLogMeal('High Protein Paneer Wrap', '1 Wrap', 420, 24, 45, 12)
    }, 1800)
  }

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault()
    if (!userInput.trim()) return
    const userMsg = { sender: 'user', text: userInput }
    setChatMessages(prev => [...prev, userMsg])
    setUserInput('')
    setIsTyping(true)

    setTimeout(() => {
      setIsTyping(false)
      let reply = "That's a solid strategy! Keep pushing your sets and ensure you hit your daily hydration target."
      if (userInput.toLowerCase().includes('water') || userInput.toLowerCase().includes('hydration')) {
        reply = "Hydration is key for muscle protein synthesis and recovery. Make sure you log at least 10-12 glasses daily. Keep ticking those targets off!"
      } else if (userInput.toLowerCase().includes('workout') || userInput.toLowerCase().includes('routine')) {
        reply = "For your Muscle Gain goal, stick to heavy progressive overload on compound lifts. Don't skip the active recovery stretches on your preferred rest day!"
      } else if (userInput.toLowerCase().includes('diet') || userInput.toLowerCase().includes('meal')) {
        reply = "Focus on reaching your protein target of 140g. Logs show you are doing great today, consider a high-protein dinner to wrap it up!"
      }
      setChatMessages(prev => [...prev, { sender: 'ai', text: reply }])
    }, 1200)
  }

  return (
    <div className="min-h-screen w-full text-slate-800 font-sans pb-12 selection:bg-black/10 selection:text-black">
      {/* Outer Cockpit Container */}
      <div className="w-full max-w-[1440px] mx-auto px-4 md:px-8 py-8">
        <div className="w-full bg-white/40 backdrop-blur-2xl border border-white/60 rounded-[48px] p-6 md:p-8 shadow-[0_32px_96px_rgba(0,0,0,0.04)] flex flex-col lg:flex-row gap-8">
          
          {/* ========================================================================= */}
          {/* LEFT SIDEBAR NAVIGATION PANEL                                             */}
          {/* ========================================================================= */}
          <aside className="w-full lg:w-[240px] bg-white/80 rounded-[32px] p-6 border border-slate-200/50 shadow-sm flex flex-col justify-between shrink-0 gap-8">
            <div className="flex flex-col gap-8">
              {/* Brand Header */}
              <div className="flex items-center gap-3 px-2">
                <div className="w-10 h-10 rounded-2xl bg-black flex items-center justify-center text-white font-bold text-lg shadow-sm">
                  ✦
                </div>
                <div>
                  <h1 className="font-display font-bold text-md text-black tracking-tight leading-none">
                    ECHOFIT
                  </h1>
                  <p className="text-[9px] text-slate-400 mt-1.5 font-bold uppercase tracking-wider">
                    AI WORKSPACE
                  </p>
                </div>
              </div>

              {/* Sidebar Tabs */}
              <nav className="flex flex-col gap-1.5">
                {[
                  { name: 'Dashboard', icon: Zap },
                  { name: 'Diet', icon: Apple },
                  { name: 'Workout', icon: Dumbbell },
                  { name: 'Progress', icon: TrendingUp },
                  { name: 'Echo', icon: MessageSquare },
                  { name: 'Profile', icon: User }
                ].map(item => {
                  const IconComp = item.icon
                  const isSelected = activeTab === item.name
                  return (
                    <button
                      key={item.name}
                      onClick={() => setActiveTab(item.name)}
                      className={`flex items-center gap-3.5 w-full px-4 py-3 rounded-2xl text-xs font-semibold tracking-wide transition-all cursor-pointer ${
                        isSelected
                          ? 'bg-black text-white shadow-md shadow-black/5'
                          : 'text-slate-500 hover:text-slate-900 hover:bg-slate-50'
                      }`}
                    >
                      <IconComp className={`w-4.5 h-4.5 ${isSelected ? 'text-white' : 'text-slate-400'}`} />
                      {item.name}
                    </button>
                  )
                })}
              </nav>
            </div>

            {/* Profile Avatar Card inside Sidebar */}
            <div className="flex items-center gap-3.5 bg-slate-50 p-3 rounded-2xl border border-slate-100">
              <div className="relative">
                <img
                  src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80"
                  alt="User"
                  className="w-10 h-10 rounded-full object-cover border border-slate-200"
                />
                <span className="absolute bottom-0 right-0 w-2.5 h-2.5 bg-emerald-500 border-2 border-white rounded-full" />
              </div>
              <div className="overflow-hidden">
                <h4 className="font-bold text-xs text-slate-800 truncate uppercase tracking-wider">{userName}</h4>
                <p className="text-[10px] text-slate-400 font-semibold truncate mt-0.5">{fitnessGoal}</p>
              </div>
            </div>
          </aside>

          {/* ========================================================================= */}
          {/* MAIN PAGE VIEWPORT AREA                                                   */}
          {/* ========================================================================= */}
          <main className="flex-1 min-w-0">
            <AnimatePresence mode="wait">
              <motion.div
                key={activeTab}
                variants={containerVariants}
                initial="hidden"
                animate="visible"
                exit="hidden"
                className="w-full flex flex-col gap-8"
              >
                {/* ========================================================================= */}
                {/* 1. DASHBOARD SCREEN OVERHAUL                                              */}
                {/* ========================================================================= */}
                {activeTab === 'Dashboard' && (
                  <div className="flex flex-col gap-8">
                    {/* Top Console Bar */}
                    <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200/40 pb-6">
                      <div>
                        <h2 className="font-display font-black text-3xl text-black tracking-tight">
                          Management
                        </h2>
                        <div className="flex items-center gap-2 mt-2 text-xs text-slate-400 font-semibold">
                          <Calendar className="w-3.5 h-3.5" />
                          <span>JUNE 1, 2026</span>
                          <span className="text-slate-300">|</span>
                          <span className="text-emerald-500 font-bold">STREAK: {streakCount} DAYS ACTIVE</span>
                        </div>
                      </div>

                      {/* Top Action controls */}
                      <div className="flex items-center gap-3">
                        <div className="relative">
                          <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
                          <input
                            type="text"
                            placeholder="Type searching..."
                            className="bg-white/80 border border-slate-200/50 pl-10 pr-4 py-2 rounded-full text-xs focus:outline-none w-[200px] shadow-sm"
                          />
                        </div>
                        <div className="w-9 h-9 rounded-full bg-white border border-slate-200/50 flex items-center justify-center shadow-sm text-slate-600 hover:text-black cursor-pointer">
                          <Settings className="w-4 h-4" />
                        </div>
                      </div>
                    </div>

                    {/* Central Dashboard Layout Grid */}
                    <div className="grid grid-cols-1 xl:grid-cols-3 gap-8">
                      {/* Left/Center Block: Timeline Task Grid & Folders */}
                      <div className="xl:col-span-2 flex flex-col gap-8">
                        {/* Timeline Tasks block */}
                        <div className="bg-white/80 rounded-[32px] border border-slate-200/40 p-6 md:p-8 shadow-sm flex flex-col gap-6">
                          <div className="flex items-center justify-between border-b border-slate-100 pb-4">
                            <h3 className="font-bold text-sm text-black uppercase tracking-wider">
                              Task Timelines
                            </h3>
                            <div className="flex bg-slate-100 p-0.5 rounded-lg border border-slate-200/30 text-[10px] font-bold">
                              {['Day', 'Week', 'Month'].map(t => (
                                <span key={t} className={`px-2.5 py-1 rounded-md cursor-pointer ${t === 'Day' ? 'bg-white text-black shadow-sm' : 'text-slate-400'}`}>
                                  {t}
                                </span>
                              ))}
                            </div>
                          </div>

                          {/* Grid timelines */}
                          <div className="flex flex-col gap-5 text-xs">
                            {[
                              { cat: 'Design', dur: 'about 2 hours', offset: 'w-[30%] ml-[10%]', color: 'bg-black text-white' },
                              { cat: 'Mobile Apps', dur: 'about 4 hours', offset: 'w-[45%] ml-[25%]', color: 'bg-black text-white' },
                              { cat: 'Infography', dur: 'about 3 hours', offset: 'w-[35%] ml-[40%]', color: 'bg-black text-white' },
                              { cat: 'Wireframes', dur: 'about 5 hours', offset: 'w-[50%] ml-[30%]', color: 'bg-black text-white' },
                              { cat: 'Team Workout', dur: 'about 2 hours', offset: 'w-[25%] ml-[50%]', color: 'bg-emerald-500 text-white' }
                            ].map((row, idx) => (
                              <div key={idx} className="flex items-center justify-between border-b border-slate-50 pb-3 last:border-0 last:pb-0">
                                <span className="w-28 font-semibold text-slate-500 truncate">{row.cat}</span>
                                <div className="flex-1 bg-slate-50 h-8 rounded-full relative overflow-hidden">
                                  <div className={`absolute h-full rounded-full flex items-center px-4 font-bold text-[9px] ${row.color} ${row.offset}`}>
                                    {row.dur}
                                  </div>
                                </div>
                              </div>
                            ))}
                          </div>
                        </div>

                        {/* File Repository Section */}
                        <div className="bg-white/80 rounded-[32px] border border-slate-200/40 p-6 md:p-8 shadow-sm flex flex-col gap-6">
                          <div className="flex items-center justify-between">
                            <h3 className="font-bold text-sm text-black uppercase tracking-wider flex items-center gap-2">
                              <FolderMinus className="w-4.5 h-4.5 text-slate-500" />
                              Documents Vault
                            </h3>
                            <span className="text-[10px] font-extrabold bg-slate-100 text-slate-600 px-2.5 py-1 rounded-full uppercase">
                              4 Items
                            </span>
                          </div>

                          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                            {[
                              { name: 'Licence on Figma templates.pdf', size: '1.2 MB' },
                              { name: 'Devspire_UI-Kit.fig', size: '24.8 MB' },
                              { name: 'Devspire redesign.word', size: '840 KB' },
                              { name: 'National Bank.fig', size: '12.5 MB' }
                            ].map((f, i) => (
                              <div key={i} className="flex items-center justify-between p-4 bg-slate-50 hover:bg-slate-100/60 border border-slate-200/30 rounded-2xl cursor-pointer transition-all">
                                <div className="truncate pr-4">
                                  <h4 className="font-semibold text-xs text-slate-800 truncate">{f.name}</h4>
                                  <p className="text-[10px] text-slate-400 mt-0.5">{f.size}</p>
                                </div>
                                <span className="text-[10px] font-bold text-black border border-slate-300 bg-white px-2 py-1 rounded-md shrink-0 shadow-sm">
                                  OPEN
                                </span>
                              </div>
                            ))}
                          </div>
                        </div>
                      </div>

                      {/* Right Block: Ongoing Projects & Black Fluid Preview Card */}
                      <div className="flex flex-col gap-8">
                        {/* Ongoing Projects Line Chart Card */}
                        <div className="bg-white/80 rounded-[32px] border border-slate-200/40 p-6 md:p-8 shadow-sm flex flex-col gap-5">
                          <div className="flex justify-between items-start">
                            <div>
                              <p className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">
                                Ongoing projects
                              </p>
                              <h3 className="font-display font-black text-2xl text-black mt-1">
                                68,5%
                              </h3>
                            </div>
                            <span className="text-[10px] font-bold text-emerald-600 bg-emerald-100/60 px-2 py-0.5 rounded">
                              +12%
                            </span>
                          </div>

                          {/* Line Chart */}
                          <div className="w-full h-16 mt-2">
                            <svg viewBox="0 0 200 50" className="w-full h-full overflow-visible">
                              <path
                                d="M 0 35 Q 35 10 70 30 T 140 15 T 200 40"
                                fill="none"
                                stroke="black"
                                strokeWidth="2.5"
                                strokeLinecap="round"
                              />
                              <circle cx="200" cy="40" r="4.5" fill="black" stroke="white" strokeWidth="1.5" />
                            </svg>
                          </div>

                          <div className="flex flex-col gap-3 text-xs border-t border-slate-100 pt-4 mt-2">
                            <div className="flex justify-between">
                              <span className="text-slate-500 font-semibold">• Finance</span>
                              <span className="font-bold text-slate-800">$48,800</span>
                            </div>
                            <div className="flex justify-between">
                              <span className="text-slate-500 font-semibold">• Design Reviews</span>
                              <span className="font-bold text-slate-800">$15,200</span>
                            </div>
                          </div>
                        </div>

                        {/* File Preview BLACK card with Liquid white silk render */}
                        <div className="bg-[#0A090F] rounded-[32px] p-6 text-white shadow-xl flex flex-col justify-between h-[360px] overflow-hidden relative">
                          {/* Top row */}
                          <div className="relative z-10 flex justify-between items-center">
                            <h4 className="font-bold text-xs uppercase tracking-wider text-slate-400">
                              File Preview
                            </h4>
                            <Share2 className="w-4 h-4 text-slate-400 hover:text-white cursor-pointer" />
                          </div>

                          {/* Center Liquid visual overlay */}
                          <div className="absolute inset-0 flex items-center justify-center opacity-70 pointer-events-none select-none">
                            <img
                              src="./assets/white_silk_bg.png"
                              alt="White silk"
                              className="w-full h-full object-cover rotate-12 scale-110"
                            />
                          </div>

                          {/* Bottom Row text */}
                          <div className="relative z-10">
                            <h3 className="font-display font-medium text-lg leading-tight mb-2">
                              VisualScope_Intro.mp4
                            </h3>
                            <div className="flex items-center justify-between text-[10px] text-slate-400 mt-2 border-t border-white/10 pt-3">
                              <span>23-08-01 • 14.8 MB</span>
                              <div className="flex items-center gap-1.5">
                                <img
                                  src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&q=80"
                                  className="w-4.5 h-4.5 rounded-full object-cover"
                                  alt="Marta"
                                />
                                <span className="font-bold text-white">Marta Adams</span>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>

                    {/* Infinite Logo Scroller at the bottom */}
                    <div className="w-full mt-4 bg-white/70 backdrop-blur-md p-4 rounded-[24px] border border-slate-200/40">
                      <MarqueeScroller />
                    </div>
                  </div>
                )}

                {/* ========================================================================= */}
                {/* 2. DIET SCREEN OVERHAUL                                                   */}
                {/* ========================================================================= */}
                {activeTab === 'Diet' && (
                  <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* Left: daily progress and logged meals */}
                    <div className="lg:col-span-2 flex flex-col gap-8">
                      <div className="bg-white/80 border border-slate-200/50 p-8 rounded-[32px] shadow-sm">
                        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-100 pb-5 mb-6">
                          <div>
                            <h2 className="font-display font-black text-2xl text-black tracking-tight">
                              Diet Planner
                            </h2>
                            <p className="text-xs text-slate-400 mt-1 font-semibold">
                              Diet Type: {dietType} | Daily Target Progress
                            </p>
                          </div>
                          <button
                            onClick={triggerCameraScan}
                            disabled={isAnalyzingImage}
                            className="flex items-center gap-2 bg-black text-white px-5 py-2.5 rounded-2xl text-xs font-semibold cursor-pointer transition-all disabled:opacity-50"
                          >
                            <Camera className="w-4 h-4" />
                            {isAnalyzingImage ? 'Analyzing...' : 'Scan Meal'}
                          </button>
                        </div>

                        {/* Progress bars */}
                        <div className="flex flex-col gap-6">
                          {[
                            { name: 'Calories', current: mealsLogged.reduce((acc, val) => acc + val.calories, 0), target: dietGoal.calories, suffix: ' kcal', fill: 'bg-black' },
                            { name: 'Protein', current: mealsLogged.reduce((acc, val) => acc + val.protein, 0), target: dietGoal.protein, suffix: 'g', fill: 'bg-emerald-500' },
                            { name: 'Carbohydrates', current: mealsLogged.reduce((acc, val) => acc + val.carbs, 0), target: dietGoal.carbs, suffix: 'g', fill: 'bg-zinc-600' },
                            { name: 'Fats', current: mealsLogged.reduce((acc, val) => acc + val.fats, 0), target: dietGoal.fats, suffix: 'g', fill: 'bg-slate-400' }
                          ].map((macro, idx) => (
                            <div key={idx}>
                              <div className="flex justify-between text-xs font-bold text-slate-700 mb-2">
                                <span>{macro.name}</span>
                                <span>{macro.current} / {macro.target}{macro.suffix}</span>
                              </div>
                              <div className="w-full bg-slate-100 h-2.5 rounded-full overflow-hidden border border-slate-200/20">
                                <div
                                  className={`${macro.fill} h-full rounded-full transition-all`}
                                  style={{ width: `${Math.min((macro.current / macro.target) * 100, 100)}%` }}
                                />
                              </div>
                            </div>
                          ))}
                        </div>
                      </div>

                      {/* Suggestions list */}
                      <div className="bg-white/80 border border-slate-200/50 p-8 rounded-[32px] shadow-sm">
                        <h3 className="font-bold text-sm text-black mb-6 uppercase tracking-wider">
                          Suggested Meal Schedule
                        </h3>
                        <div className="flex flex-col gap-4">
                          {[
                            { title: 'Breakfast', name: 'Oatmeal Porridge with Almonds & Honey', cals: 360, protein: 12 },
                            { title: 'Lunch', name: 'Sautéed Quinoa with Tofu & Mixed Veggies', cals: 480, protein: 24 },
                            { title: 'Snacks', name: 'Protein Shake & Raw Walnut Mix', cals: 300, protein: 30 },
                            { title: 'Dinner', name: 'Baked Salmon with Broccoli & Rice', cals: 520, protein: 38 }
                          ].map((s, idx) => (
                            <div key={idx} className="flex flex-col md:flex-row md:items-center justify-between p-4 bg-slate-50 rounded-2xl border border-slate-200/30 hover:border-slate-200 transition-all gap-4">
                              <div>
                                <span className="text-[10px] font-bold text-black border border-slate-300 bg-white px-2 py-0.5 rounded uppercase tracking-wider">
                                  {s.title}
                                </span>
                                <h4 className="font-semibold text-sm text-slate-800 mt-2">
                                  {s.name}
                                </h4>
                                <p className="text-xs text-slate-400 mt-0.5">
                                  {s.cals} kcal | {s.protein}g Protein | Low Budget
                                </p>
                              </div>
                              <button
                                onClick={() => handleLogMeal(s.name, '1 serving', s.cals, s.protein)}
                                className="flex items-center gap-1.5 bg-white hover:bg-slate-50 text-black border border-slate-200 px-4 py-2 rounded-xl text-xs font-semibold cursor-pointer transition-all shadow-sm shrink-0"
                              >
                                <Plus className="w-3.5 h-3.5" />
                                Log Suggestion
                              </button>
                            </div>
                          ))}
                        </div>
                      </div>
                    </div>

                    {/* Right: meal logs list and adding */}
                    <div className="flex flex-col gap-8">
                      {/* Manual Add Card */}
                      <div className="bg-white/80 border border-slate-200/50 p-8 rounded-[32px] shadow-sm">
                        <h3 className="font-bold text-sm text-black mb-6 uppercase tracking-wider">
                          Add Meal Manually
                        </h3>
                        <form onSubmit={handleManualMealSubmit} className="flex flex-col gap-4">
                          <div>
                            <label className="text-xs font-bold text-slate-500">Meal Name</label>
                            <input
                              type="text"
                              placeholder="e.g. Protein shake"
                              value={manualFoodName}
                              onChange={e => setManualFoodName(e.target.value)}
                              className="w-full mt-2 px-4 py-2.5 border border-slate-200 rounded-xl text-xs focus:outline-none focus:ring-1 focus:ring-black/20"
                            />
                          </div>

                          <div className="grid grid-cols-2 gap-4">
                            <div>
                              <label className="text-xs font-bold text-slate-500">Est. Calories</label>
                              <input
                                type="number"
                                placeholder="kcal"
                                value={manualCals}
                                onChange={e => setManualCals(e.target.value)}
                                className="w-full mt-2 px-4 py-2.5 border border-slate-200 rounded-xl text-xs focus:outline-none focus:ring-1 focus:ring-black/20"
                              />
                            </div>
                            <div>
                              <label className="text-xs font-bold text-slate-500">Protein (g)</label>
                              <input
                                type="number"
                                placeholder="grams"
                                value={manualPro}
                                onChange={e => setManualPro(e.target.value)}
                                className="w-full mt-2 px-4 py-2.5 border border-slate-200 rounded-xl text-xs focus:outline-none focus:ring-1 focus:ring-black/20"
                              />
                            </div>
                          </div>

                          <button
                            type="submit"
                            className="w-full mt-4 py-3.5 bg-black text-white font-bold rounded-xl text-xs cursor-pointer hover:bg-slate-800 transition-all shadow-sm"
                          >
                            SAVE MEAL LOG
                          </button>
                        </form>
                      </div>

                      {/* Log history list */}
                      <div className="bg-white/80 border border-slate-200/50 p-8 rounded-[32px] shadow-sm">
                        <h3 className="font-bold text-sm text-black mb-6 uppercase tracking-wider">
                          Logged Today
                        </h3>

                        <div className="flex flex-col gap-3 max-h-[300px] overflow-y-auto">
                          {mealsLogged.length === 0 ? (
                            <p className="text-xs text-slate-400 text-center py-6">
                              No meals logged today yet.
                            </p>
                          ) : (
                            mealsLogged.map(meal => (
                              <div key={meal.id} className="flex items-center justify-between p-3.5 bg-slate-50 rounded-2xl border border-slate-100">
                                <div>
                                  <h4 className="font-bold text-xs text-slate-800">
                                    {meal.name}
                                  </h4>
                                  <p className="text-[10px] text-slate-400 mt-1">
                                    {meal.calories} kcal | {meal.protein}g Protein
                                  </p>
                                </div>
                                <button
                                  onClick={() => setMealsLogged(mealsLogged.filter(m => m.id !== meal.id))}
                                  className="text-slate-400 hover:text-rose-500 transition-colors p-1"
                                >
                                  <Trash2 className="w-3.5 h-3.5" />
                                </button>
                              </div>
                            ))
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                )}

                {/* ========================================================================= */}
                {/* 3. WORKOUT SCREEN OVERHAUL                                                */}
                {/* ========================================================================= */}
                {activeTab === 'Workout' && (
                  <div className="flex flex-col gap-8">
                    <div className="bg-white/80 border border-slate-200/50 p-8 rounded-[32px] shadow-sm">
                      <div className="border-b border-slate-100 pb-5 mb-8">
                        <h2 className="font-display font-black text-2xl text-black tracking-tight">
                          Workout Studio
                        </h2>
                        <p className="text-xs text-slate-400 mt-1 font-semibold">
                          Active recovery yoga splits & cardiovascular dance routines.
                        </p>
                      </div>

                      <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">
                        {/* Yoga section */}
                        <div>
                          <h3 className="font-bold text-xs text-slate-400 mb-6 uppercase tracking-wider flex items-center gap-2">
                            <span className="w-2.5 h-2.5 rounded-full bg-black" />
                            Flexibility Flow Yoga
                          </h3>

                          <div className="flex flex-col gap-4">
                            {[
                              { name: 'Morning Flexibility Flow', mins: 15, cals: 80, level: 'Easy' },
                              { name: 'Back Pain Relief Yoga', mins: 10, cals: 60, level: 'Easy' },
                              { name: 'Rest-Day Deep Muscle Recovery', mins: 20, cals: 110, level: 'Medium' }
                            ].map((s, idx) => (
                              <div
                                key={idx}
                                onClick={() => {
                                  setActiveVideoSession(s)
                                  setVideoTimer(s.mins * 60)
                                  setVideoIsPlaying(false)
                                }}
                                className="flex items-center justify-between p-4 bg-slate-50 hover:bg-slate-100/70 border border-slate-200/20 rounded-2xl cursor-pointer transition-all"
                              >
                                <div>
                                  <h4 className="font-bold text-xs text-slate-800">{s.name}</h4>
                                  <p className="text-[10px] text-slate-400 mt-1">
                                    {s.mins} mins • Burn: {s.cals} kcal • {s.level}
                                  </p>
                                </div>
                                <div className="w-8 h-8 rounded-full bg-black/5 hover:bg-black hover:text-white transition-all flex items-center justify-center text-black">
                                  <Play className="w-3 h-3 fill-current" />
                                </div>
                              </div>
                            ))}
                          </div>
                        </div>

                        {/* Zumba section */}
                        <div>
                          <h3 className="font-bold text-xs text-slate-400 mb-6 uppercase tracking-wider flex items-center gap-2">
                            <span className="w-2.5 h-2.5 rounded-full bg-black" />
                            Cardio Zumba Dance
                          </h3>

                          <div className="flex flex-col gap-4">
                            {[
                              { name: 'Beginner Zumba Beat Dance', mins: 15, cals: 160, level: 'Easy' },
                              { name: 'Latin Salsa Cardio Blast', mins: 20, cals: 220, level: 'Medium' },
                              { name: '10-Minute Rapid Beat Burn', mins: 10, cals: 120, level: 'Hard' }
                            ].map((s, idx) => (
                              <div
                                key={idx}
                                onClick={() => {
                                  setActiveVideoSession(s)
                                  setVideoTimer(s.mins * 60)
                                  setVideoIsPlaying(false)
                                }}
                                className="flex items-center justify-between p-4 bg-slate-50 hover:bg-slate-100/70 border border-slate-200/20 rounded-2xl cursor-pointer transition-all"
                              >
                                <div>
                                  <h4 className="font-bold text-xs text-slate-800">{s.name}</h4>
                                  <p className="text-[10px] text-slate-400 mt-1">
                                    {s.mins} mins • Burn: {s.cals} kcal • {s.level}
                                  </p>
                                </div>
                                <div className="w-8 h-8 rounded-full bg-black/5 hover:bg-black hover:text-white transition-all flex items-center justify-center text-black">
                                  <Play className="w-3 h-3 fill-current" />
                                </div>
                              </div>
                            ))}
                          </div>
                        </div>
                      </div>
                    </div>

                    {/* Media Player Stream simulation overlay */}
                    {activeVideoSession && (
                      <div className="fixed inset-0 z-50 bg-black/30 backdrop-blur-sm flex items-center justify-center p-4">
                        <div className="bg-white rounded-[32px] border border-slate-200/50 p-6 md:p-8 max-w-lg w-full shadow-2xl flex flex-col gap-6">
                          <div className="flex items-center justify-between border-b border-slate-100 pb-4">
                            <h3 className="font-bold text-sm text-black">
                              {activeVideoSession.name}
                            </h3>
                            <button
                              onClick={() => {
                                setActiveVideoSession(null)
                                setVideoIsPlaying(false)
                              }}
                              className="text-slate-400 hover:text-black font-extrabold text-xs"
                            >
                              Close
                            </button>
                          </div>

                          {/* Video screen representation */}
                          <div className="relative w-full h-[220px] rounded-2xl bg-slate-900 overflow-hidden flex items-center justify-center border border-slate-200 shadow-inner">
                            <img
                              src="./assets/white_silk_bg.png"
                              alt="White silk"
                              className="absolute inset-0 w-full h-full object-cover opacity-10"
                            />
                            {videoIsPlaying ? (
                              <div className="relative z-10 text-center">
                                <span className="w-3 h-3 bg-red-600 rounded-full inline-block animate-ping mr-2" />
                                <span className="text-xs text-white font-bold tracking-widest uppercase">STREAMING WORKOUT...</span>
                              </div>
                            ) : (
                              <div className="text-center p-4 relative z-10">
                                <Play className="w-12 h-12 text-white mx-auto opacity-70 mb-2 cursor-pointer hover:opacity-100 transition-opacity" onClick={() => setVideoIsPlaying(true)} />
                                <p className="text-xs text-slate-400">Stream Paused. Press play to start.</p>
                              </div>
                            )}
                            <div className="absolute bottom-4 left-4 bg-black/60 backdrop-blur-md px-3 py-1 rounded-md text-[9px] font-bold text-white tracking-wider">
                              {videoIsPlaying ? 'LIVE STREAMING' : 'PAUSED'}
                            </div>
                          </div>

                          <div className="flex flex-col gap-2">
                            <div className="flex justify-between text-xs text-slate-500 font-semibold">
                              <span>Progress Timer</span>
                              <span>{Math.floor(videoTimer / 60)}:{(videoTimer % 60).toString().padStart(2, '0')} remaining</span>
                            </div>
                            <div className="w-full bg-slate-100 h-1.5 rounded-full overflow-hidden border border-slate-200/20">
                              <div
                                className="bg-black h-full rounded-full transition-all"
                                style={{ width: `${((activeVideoSession.mins * 60 - videoTimer) / (activeVideoSession.mins * 60)) * 100}%` }}
                              />
                            </div>
                          </div>

                          <div className="flex gap-2">
                            <button
                              onClick={() => setVideoIsPlaying(!videoIsPlaying)}
                              className="flex-1 py-3.5 bg-slate-100 hover:bg-slate-200 text-black border border-slate-200 rounded-2xl text-xs font-bold cursor-pointer transition-all"
                            >
                              {videoIsPlaying ? 'PAUSE SESSION' : 'RESUME SESSION'}
                            </button>
                            <button
                              onClick={() => {
                                setWorkoutCompleted(true)
                                setActiveVideoSession(null)
                                setVideoIsPlaying(false)
                              }}
                              className="flex-1 py-3.5 bg-black text-white font-bold rounded-2xl text-xs cursor-pointer hover:bg-slate-800 transition-all shadow-sm"
                            >
                              MARK COMPLETED
                            </button>
                          </div>
                        </div>
                      </div>
                    )}
                  </div>
                )}

                {/* ========================================================================= */}
                {/* 4. PROGRESS SCREEN OVERHAUL                                               */}
                {/* ========================================================================= */}
                {activeTab === 'Progress' && (
                  <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* Charts log */}
                    <div className="lg:col-span-2 bg-white/80 border border-slate-200/50 p-8 rounded-[32px] shadow-sm flex flex-col gap-6">
                      <div className="border-b border-slate-100 pb-5">
                        <h2 className="font-display font-black text-2xl text-black tracking-tight">
                          Weight Logger
                        </h2>
                        <p className="text-xs text-slate-400 mt-1 font-semibold">
                          Current Weight: {progressWeight} kg | Target Goal: 72.0 kg
                        </p>
                      </div>

                      {/* SVG Chart styled in light mockup format */}
                      <div className="w-full h-[200px] border-b border-slate-150 relative mt-4">
                        <svg viewBox="0 0 400 150" className="w-full h-full overflow-visible">
                          <line x1="0" y1="20" x2="400" y2="20" stroke="#f1f5f9" strokeWidth="1" />
                          <line x1="0" y1="70" x2="400" y2="70" stroke="#f1f5f9" strokeWidth="1" />
                          <line x1="0" y1="120" x2="400" y2="120" stroke="#f1f5f9" strokeWidth="1" />

                          <path
                            d="M 20 120 L 120 90 L 220 70 L 320 50"
                            fill="none"
                            stroke="black"
                            strokeWidth="3"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          />

                          <circle cx="20" cy="120" r="4.5" fill="white" stroke="black" strokeWidth="2.5" />
                          <circle cx="120" cy="90" r="4.5" fill="white" stroke="black" strokeWidth="2.5" />
                          <circle cx="220" cy="70" r="4.5" fill="white" stroke="black" strokeWidth="2.5" />
                          <circle cx="320" cy="50" r="4.5" fill="white" stroke="black" strokeWidth="2.5" />

                          <text x="20" y="142" fontSize="9" fill="#94a3b8" textAnchor="middle" fontWeight="600">May 20</text>
                          <text x="120" y="142" fontSize="9" fill="#94a3b8" textAnchor="middle" fontWeight="600">May 24</text>
                          <text x="220" y="142" fontSize="9" fill="#94a3b8" textAnchor="middle" fontWeight="600">May 28</text>
                          <text x="320" y="142" fontSize="9" fill="#94a3b8" textAnchor="middle" fontWeight="600">Jun 01</text>
                        </svg>
                      </div>

                      <div className="flex items-center gap-4 mt-4">
                        <input
                          type="number"
                          placeholder="kg"
                          value={newWeightInput}
                          onChange={e => setNewWeightInput(e.target.value)}
                          className="px-4 py-2.5 border border-slate-200 rounded-xl text-xs focus:outline-none focus:ring-1 focus:ring-black/20 w-44"
                        />
                        <button
                          onClick={() => {
                            if (!newWeightInput) return
                            const newWeight = parseFloat(newWeightInput)
                            setProgressWeight(newWeightInput)
                            setWeightLogs([...weightLogs, { date: 'Today', weight: newWeight }])
                            setNewWeightInput('')
                          }}
                          className="bg-black hover:bg-slate-800 text-white px-5 py-2.5 rounded-xl text-xs font-semibold cursor-pointer transition-all shadow-sm"
                        >
                          Log Weight
                        </button>
                      </div>
                    </div>

                    {/* Photos logs */}
                    <div className="bg-white/80 border border-slate-200/50 p-8 rounded-[32px] shadow-sm flex flex-col gap-6">
                      <h3 className="font-bold text-sm text-black mb-4 uppercase tracking-wider flex items-center gap-2">
                        <Lock className="w-4 h-4 text-slate-400" />
                        Photos Vault
                      </h3>

                      <div className="grid grid-cols-2 gap-4">
                        {progressPhotos.map((url, idx) => (
                          <div key={idx} className="relative rounded-2xl overflow-hidden h-36 border border-slate-200 shadow-sm group">
                            <img src={url} alt="Progress" className="w-full h-full object-cover transition-transform group-hover:scale-105" />
                            <div className="absolute bottom-2 left-2 bg-black/60 px-2 py-0.5 rounded text-[9px] font-bold text-white uppercase">
                              Week {idx + 1}
                            </div>
                          </div>
                        ))}
                        <div className="border-2 border-dashed border-slate-200 hover:border-slate-350 rounded-2xl flex flex-col items-center justify-center h-36 cursor-pointer bg-slate-50/50 hover:bg-slate-50 transition-all gap-1.5">
                          <Camera className="w-6 h-6 text-slate-400" />
                          <span className="text-[10px] font-bold text-slate-500">Upload Photo</span>
                        </div>
                      </div>
                    </div>
                  </div>
                )}

                {/* ========================================================================= */}
                {/* 5. ECHO CHAT SCREEN OVERHAUL                                              */}
                {/* ========================================================================= */}
                {activeTab === 'Echo' && (
                  <div className="w-full max-w-3xl mx-auto bg-white border border-slate-200/50 rounded-[32px] shadow-[0_24px_64px_rgba(0,0,0,0.02)] overflow-hidden flex flex-col h-[520px]">
                    {/* Header */}
                    <div className="bg-white px-6 py-4 flex items-center justify-between border-b border-slate-100">
                      <div className="flex items-center gap-3">
                        <div className="w-8 h-8 rounded-full bg-black flex items-center justify-center text-white font-bold text-sm">
                          E
                        </div>
                        <div>
                          <h3 className="font-bold text-xs text-black">
                            Coach Echo
                          </h3>
                          <p className="text-[9px] text-emerald-600 font-bold uppercase mt-0.5">
                            AI AGENT ONLINE
                          </p>
                        </div>
                      </div>
                    </div>

                    {/* Chat Bubble Thread */}
                    <div className="flex-1 p-6 overflow-y-auto flex flex-col gap-4 bg-slate-50/50">
                      {chatMessages.map((m, idx) => (
                        <div
                          key={idx}
                          className={`flex flex-col max-w-[80%] ${
                            m.sender === 'user' ? 'align-self-end items-end ml-auto' : 'align-self-start items-start mr-auto'
                          }`}
                        >
                          <div
                            className={`p-4 rounded-[20px] text-xs leading-relaxed border ${
                              m.sender === 'user'
                                ? 'bg-black text-white border-black rounded-tr-none shadow-sm shadow-black/5'
                                : 'bg-white text-slate-800 border-slate-200/60 rounded-tl-none shadow-sm shadow-slate-100/50'
                            }`}
                          >
                            {m.text}
                          </div>
                        </div>
                      ))}
                      {isTyping && (
                        <div className="align-self-start items-start mr-auto max-w-[80%] flex items-center gap-1.5 p-3 bg-white border border-slate-200/40 rounded-2xl rounded-tl-none shadow-sm shadow-slate-100/50">
                          <span className="w-1.5 h-1.5 rounded-full bg-slate-400 animate-bounce" />
                          <span className="w-1.5 h-1.5 rounded-full bg-slate-400 animate-bounce [animation-delay:0.2s]" />
                          <span className="w-1.5 h-1.5 rounded-full bg-slate-400 animate-bounce [animation-delay:0.4s]" />
                        </div>
                      )}
                    </div>

                    {/* Footer Chat Input */}
                    <form onSubmit={handleSendMessage} className="p-4 bg-white border-t border-slate-100 flex gap-2">
                      <input
                        type="text"
                        placeholder="Ask Coach Echo a question..."
                        value={userInput}
                        onChange={e => setUserInput(e.target.value)}
                        className="flex-1 px-4 py-3 border border-slate-200 rounded-xl text-xs focus:outline-none focus:ring-1 focus:ring-black/20"
                      />
                      <button
                        type="submit"
                        className="bg-black hover:bg-slate-800 text-white p-3.5 rounded-xl cursor-pointer transition-all shadow-sm"
                      >
                        <Send className="w-4 h-4" />
                      </button>
                    </form>
                  </div>
                )}

                {/* ========================================================================= */}
                {/* 6. PROFILE SCREEN OVERHAUL                                                */}
                {/* ========================================================================= */}
                {activeTab === 'Profile' && (
                  <div className="max-w-2xl mx-auto bg-white border border-slate-200/50 p-8 rounded-[32px] shadow-sm">
                    <h2 className="font-display font-black text-2xl text-black border-b border-slate-100 pb-5 mb-6">
                      Configuration
                    </h2>

                    <div className="flex flex-col gap-6">
                      <div className="grid grid-cols-2 gap-4">
                        <div>
                          <label className="text-xs font-bold text-slate-500">Profile Name</label>
                          <input
                            type="text"
                            value={userName}
                            onChange={e => setUserName(e.target.value)}
                            className="w-full mt-2 px-4 py-2.5 border border-slate-200 rounded-xl text-xs focus:outline-none focus:ring-1 focus:ring-black/20"
                          />
                        </div>
                        <div>
                          <label className="text-xs font-bold text-slate-500">Primary Goal</label>
                          <select
                            value={fitnessGoal}
                            onChange={e => setFitnessGoal(e.target.value)}
                            className="w-full mt-2 px-4 py-2.5 border border-slate-200 rounded-xl text-xs focus:outline-none focus:ring-1 focus:ring-black/20 bg-white"
                          >
                            <option>General Fitness</option>
                            <option>Muscle Gain</option>
                            <option>Fat Loss</option>
                            <option>Active Endurance</option>
                          </select>
                        </div>
                      </div>

                      <div className="grid grid-cols-2 gap-4">
                        <div>
                          <label className="text-xs font-bold text-slate-500">Dietary Preferences</label>
                          <select
                            value={dietType}
                            onChange={e => setDietType(e.target.value)}
                            className="w-full mt-2 px-4 py-2.5 border border-slate-200 rounded-xl text-xs focus:outline-none focus:ring-1 focus:ring-black/20 bg-white"
                          >
                            <option>Veg / Vegetarian</option>
                            <option>Non-Vegetarian</option>
                            <option>Vegan</option>
                            <option>Keto Low-Carb</option>
                          </select>
                        </div>
                        <div>
                          <label className="text-xs font-bold text-slate-500">Preferred Rest Day</label>
                          <select
                            value={preferredRestDay}
                            onChange={e => setPreferredRestDay(e.target.value)}
                            className="w-full mt-2 px-4 py-2.5 border border-slate-200 rounded-xl text-xs focus:outline-none focus:ring-1 focus:ring-black/20 bg-white"
                          >
                            <option>Saturday</option>
                            <option>Sunday</option>
                            <option>Monday</option>
                            <option>Rest Mode Disabled</option>
                          </select>
                        </div>
                      </div>

                      <button
                        onClick={() => alert('Profile settings saved successfully!')}
                        className="w-full mt-4 py-3.5 bg-black text-white font-bold rounded-xl text-xs cursor-pointer hover:bg-slate-800 transition-all shadow-sm"
                      >
                        SAVE CONFIGURATION
                      </button>
                    </div>
                  </div>
                )}
              </motion.div>
            </AnimatePresence>
          </main>

        </div>
      </div>
    </div>
  )
}

export default App
