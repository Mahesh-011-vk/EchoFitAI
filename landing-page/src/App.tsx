import { useState, useEffect } from 'react'
import { motion, AnimatePresence } from 'motion/react'
import {
  Plus,
  Trash2,
  Camera,
  Flame,
  Droplet,
  Dumbbell,
  Apple,
  TrendingUp,
  MessageSquare,
  Send,
  Zap,
  Play,
  Calendar,
  Lock,
  Share2,
  Settings,
  Info,
  Bell,
  CloudLightning,
  Check,
  ArrowRight,
  ShieldCheck,
  Award,
  RefreshCw
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

interface Reminder {
  id: string
  name: string
  time: string
  isEnabled: boolean
}

function App() {
  const [activeTab, setActiveTab] = useState('HOME')

  // Shared state for app data simulation
  const [waterGlasses, setWaterGlasses] = useState(6)
  const [waterTarget] = useState(12)
  const [workoutCompleted, setWorkoutCompleted] = useState(false)
  const [mealsLogged, setMealsLogged] = useState<Meal[]>([
    { id: '1', name: 'Scrambled Eggs (3) & Toast', portion: '1 Plate', calories: 380, protein: 22, carbs: 24, fats: 16 }
  ])
  const streakCount = 5

  // Diet Tab States
  const [manualFoodName, setManualFoodName] = useState('')
  const [manualCals, setManualCals] = useState('')
  const [manualPro, setManualPro] = useState('')
  const [manualCarbs, setManualCarbs] = useState('')
  const [manualFats, setManualFats] = useState('')
  const [isAnalyzingImage, setIsAnalyzingImage] = useState(false)
  const [isManualAddOpen, setIsManualAddOpen] = useState(false)
  const dietGoal = { calories: 2200, protein: 140, carbs: 250, fats: 75 }

  // Workout Tab States
  const [workoutSubTab, setWorkoutSubTab] = useState('PLAN') // PLAN, YOGA_ZUMBA
  const [activeVideoSession, setActiveVideoSession] = useState<any>(null)
  const [videoTimer, setVideoTimer] = useState(0)
  const [videoIsPlaying, setVideoIsPlaying] = useState(false)

  // Progress Tab States
  const [progressWeight, setProgressWeight] = useState('74.5')
  const [weightLogs, setWeightLogs] = useState<{ date: string; weight: number }[]>([
    { date: 'Mon', weight: 75.8 },
    { date: 'Tue', weight: 75.2 },
    { date: 'Wed', weight: 74.9 },
    { date: 'Thu', weight: 74.7 },
    { date: 'Fri', weight: 74.5 }
  ])
  const [newWeightInput, setNewWeightInput] = useState('')
  const [photosList, setPhotosList] = useState<{ position: string; url: string }[]>([
    { position: 'Front', url: 'https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=400&q=80' }
  ])
  const [isUploadingPhoto, setIsUploadingPhoto] = useState<string | null>(null)
  const [isAnalyzingPhysique, setIsAnalyzingPhysique] = useState(false)
  const [physiqueReport, setPhysiqueReport] = useState<string | null>(null)

  // Chat Tab States
  const [chatMessages, setChatMessages] = useState<any[]>([
    { sender: 'ai', text: "Hey champion! I'm Coach Echo, your AI fitness guide. Ask me anything about your current workout program, nutrition budgets, or active recovery splits!" }
  ])
  const [userInput, setUserInput] = useState('')
  const [isTyping, setIsTyping] = useState(false)

  // Settings Tab States
  const [reminders, setReminders] = useState<Reminder[]>([
    { id: 'breakfast', name: 'Breakfast Reminder', time: '08:30 AM', isEnabled: true },
    { id: 'lunch', name: 'Lunch Reminder', time: '01:30 PM', isEnabled: true },
    { id: 'snack', name: 'Evening Snack Reminder', time: '05:00 PM', isEnabled: true },
    { id: 'workout', name: 'Workout Gym/Home Time', time: '07:00 PM', isEnabled: true },
    { id: 'dinner', name: 'Dinner Reminder', time: '08:30 PM', isEnabled: true }
  ])
  const [isSyncingCloud, setIsSyncingCloud] = useState(false)

  // Video streaming simulation timer
  useEffect(() => {
    let interval: any
    if (videoIsPlaying && videoTimer > 0) {
      interval = setInterval(() => {
        setVideoTimer(t => t - 1)
      }, 1000)
    } else if (videoTimer === 0 && videoIsPlaying) {
      setVideoIsPlaying(false)
      setWorkoutCompleted(true)
      setActiveVideoSession(null)
      alert("Workout session completed! XP gained.")
    }
    return () => clearInterval(interval)
  }, [videoIsPlaying, videoTimer])

  // Handles adding logged food item
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
    setMealsLogged(prev => [...prev, newMeal])
  }

  // Handle manual food form submission
  const handleManualMealSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!manualFoodName) return
    handleLogMeal(
      manualFoodName,
      '1 serving',
      parseInt(manualCals) || 350,
      parseInt(manualPro) || 20,
      parseInt(manualCarbs) || 40,
      parseInt(manualFats) || 8
    )
    setManualFoodName('')
    setManualCals('')
    setManualPro('')
    setManualCarbs('')
    setManualFats('')
    setIsManualAddOpen(false)
  }

  // Camera scan simulation
  const triggerCameraScan = () => {
    setIsAnalyzingImage(true)
    setTimeout(() => {
      setIsAnalyzingImage(false)
      handleLogMeal('High Protein Paneer Wrap', '1 Wrap', 420, 24, 45, 12)
    }, 1800)
  }

  // Simulated AI response classification
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
      const lowerInput = userInput.toLowerCase()
      if (lowerInput.includes('water') || lowerInput.includes('hydration')) {
        reply = "Hydration is key for muscle protein synthesis and recovery. Make sure you log at least 10-12 glasses daily. Keep ticking those targets off!"
      } else if (lowerInput.includes('workout') || lowerInput.includes('routine') || lowerInput.includes('bench press')) {
        reply = "For your Muscle Gain goal, stick to heavy progressive overload on compound lifts. Don't skip the active recovery stretches on your preferred rest day!"
      } else if (lowerInput.includes('diet') || lowerInput.includes('meal') || lowerInput.includes('protein')) {
        reply = "Focus on reaching your protein target of 140g. Logs show you are doing great today, consider a high-protein snack like sprouts or paneer!"
      } else if (lowerInput.includes('hi') || lowerInput.includes('hello')) {
        reply = "Hello champion! I am Coach Echo, running fully on-device. Ask me anything about your diet plans, workout modifications, or progress tracking!"
      }
      setChatMessages(prev => [...prev, { sender: 'ai', text: reply }])
    }, 1200)
  }

  // Simulate Cloud DB Firestore Sync
  const handleForceSync = () => {
    setIsSyncingCloud(true)
    setTimeout(() => {
      setIsSyncingCloud(false)
      alert("Cloud sync complete! Rooms DB synced with Firestore successfully.")
    }, 2000)
  }

  // Simulate Photo Checkins
  const handlePhotoUpload = (position: string) => {
    setIsUploadingPhoto(position)
    setTimeout(() => {
      const urls: Record<string, string> = {
        Front: 'https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=400&q=80',
        Side: 'https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=400&q=80',
        Back: 'https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=400&q=80'
      }
      if (!photosList.find(p => p.position === position)) {
        setPhotosList(prev => [...prev, { position, url: urls[position] }])
      }
      setIsUploadingPhoto(null)
    }, 1500)
  }

  // Simulate Physique Analysis
  const handleAnalyzePhysique = () => {
    setIsAnalyzingPhysique(true)
    setTimeout(() => {
      setIsAnalyzingPhysique(false)
      setPhysiqueReport(
        "### AI Physique Assessment (Echo local)\n" +
        "* **Body Fat**: Visual outline indicates a drop in abdominal fat.\n" +
        "* **Muscle Tone**: Toning noticed in shoulders and upper chest.\n" +
        "* **Guidance**: Continue with your current 2200 kcal deficit."
      )
    }, 2000)
  }

  return (
    <div className="min-h-screen w-full bg-gradient-to-br from-[#0F0C20] via-[#05040B] to-[#0A0718] text-slate-100 font-sans flex items-center justify-center p-4 md:p-8 relative overflow-hidden">
      
      {/* Blurred background decorative elements */}
      <div className="absolute w-[400px] h-[400px] bg-amber-500/10 blur-[120px] rounded-full top-[-100px] left-[-100px] pointer-events-none" />
      <div className="absolute w-[500px] h-[500px] bg-emerald-500/5 blur-[150px] rounded-full bottom-[-150px] right-[-100px] pointer-events-none" />

      <div className="w-full max-w-[1200px] grid grid-cols-1 lg:grid-cols-12 gap-8 items-center relative z-10">
        
        {/* ========================================================================= */}
        {/* LEFT PANEL: PRODUCT BRIEFING (Hidden on smaller screens, wraps nicely)     */}
        {/* ========================================================================= */}
        <div className="lg:col-span-7 flex flex-col gap-6 text-left max-w-xl mx-auto lg:mx-0 pr-0 lg:pr-8 py-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-2xl bg-gradient-to-br from-amber-400 to-amber-600 flex items-center justify-center text-black font-extrabold text-xl shadow-lg shadow-amber-500/20">
              ✦
            </div>
            <div>
              <h1 className="font-display font-black text-2xl tracking-wider text-white">ECHOFIT</h1>
              <p className="text-[10px] text-amber-400 font-extrabold tracking-widest uppercase">Mobile Emulator Preview</p>
            </div>
          </div>

          <h2 className="font-display font-black text-4xl sm:text-5xl text-white tracking-tight leading-none mt-2">
            Experience EchoFit <br/>
            <span className="bg-gradient-to-r from-amber-400 via-yellow-300 to-emerald-400 bg-clip-text text-transparent">Exactly as it Runs</span>
          </h2>

          <p className="text-slate-400 text-sm leading-relaxed">
            Welcome to the fully interactive mobile web preview of EchoFit. This emulator-styled console is programmed to work exactly like the Android build. Try logging meals, updating settings, triggering mock vision scans, and auditing logs below.
          </p>

          <div className="grid grid-cols-2 gap-4 mt-2">
            {[
              { text: "Compose UI Screens", desc: "True Pixel Mock" },
              { text: "Dual-Engine AI Coach", desc: "Local Text Classifier" },
              { text: "WorkManager Alerts", desc: "Interactive Reminders" },
              { text: "Firebase Cloud Sync", desc: "Relational room DB" }
            ].map((feature, i) => (
              <div key={i} className="p-3 bg-white/5 border border-white/10 rounded-2xl flex flex-col gap-1">
                <span className="text-xs font-bold text-white flex items-center gap-1.5">
                  <span className="w-1.5 h-1.5 rounded-full bg-amber-400" />
                  {feature.text}
                </span>
                <span className="text-[10px] text-slate-400 font-semibold">{feature.desc}</span>
              </div>
            ))}
          </div>

          <div className="flex flex-wrap items-center gap-4 mt-4">
            <a
              href="https://github.com/Mahesh-011-vk/EchoFitAI/releases"
              target="_blank"
              rel="noreferrer"
              className="flex items-center gap-2 bg-gradient-to-r from-amber-400 to-amber-500 hover:from-amber-500 hover:to-amber-600 text-black px-6 py-3 rounded-2xl text-xs font-black tracking-wide shadow-xl shadow-amber-500/20 cursor-pointer transition-all"
            >
              DOWNLOAD TESTING APK
              <ArrowRight className="w-4 h-4" />
            </a>
            <a
              href="https://github.com/Mahesh-011-vk/EchoFitAI"
              target="_blank"
              rel="noreferrer"
              className="flex items-center gap-2 border border-white/10 bg-white/5 hover:bg-white/10 text-white px-5 py-3 rounded-2xl text-xs font-bold transition-all"
            >
              VIEW ON GITHUB
            </a>
          </div>

          <div className="w-full mt-4 border-t border-white/5 pt-6 hidden sm:block">
            <MarqueeScroller />
          </div>
        </div>

        {/* ========================================================================= */}
        {/* RIGHT PANEL: EMULATOR CONTAINER SHELL                                     */}
        {/* ========================================================================= */}
        <div className="lg:col-span-5 flex justify-center py-4 w-full">
          
          {/* Smartphone mockup outline */}
          <div className="w-[375px] h-[812px] bg-[#0E0D14] border-[12px] border-[#181622] rounded-[48px] shadow-[0_30px_100px_rgba(0,0,0,0.8)] relative flex flex-col overflow-hidden select-none">
            
            {/* Top Speaker Notch and Camera bar */}
            <div className="w-[120px] h-[22px] bg-[#181622] rounded-b-[16px] absolute top-0 left-1/2 -translate-x-1/2 z-50 flex items-center justify-center gap-1.5">
              <span className="w-12 h-1 bg-black/40 rounded-full" />
              <span className="w-2.5 h-2.5 rounded-full bg-slate-950 border border-slate-800" />
            </div>

            {/* Mobile Status Bar */}
            <div className="h-[32px] px-6 pt-2 flex justify-between items-center text-[10px] text-white/80 font-bold select-none z-40 bg-[#09080F]">
              <span>12:10</span>
              <div className="flex items-center gap-1.5">
                <span>5G</span>
                <span>📶</span>
                <span>🔋 98%</span>
              </div>
            </div>

            {/* Simulated app background (white silk texture base) */}
            <div className="absolute inset-0 bg-cover bg-center opacity-10 pointer-events-none" style={{ backgroundImage: "url('./assets/white_silk_bg.png')" }} />

            {/* ========================================================================= */}
            {/* CORE APPLICATION VIEWS                                                    */}
            {/* ========================================================================= */}
            <div className="flex-1 overflow-y-auto px-4 py-4 flex flex-col gap-4 bg-[#09080E] relative z-20 scrollbar-none">
              
              {/* --- TABS RENDERING --- */}
              <AnimatePresence mode="wait">
                <motion.div
                  key={activeTab}
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -10 }}
                  transition={{ duration: 0.2 }}
                  className="flex flex-col gap-4 w-full min-h-full"
                >
                  {/* ============================================================= */}
                  {/* TAB: HOME                                                     */}
                  {/* ============================================================= */}
                  {activeTab === 'HOME' && (
                    <div className="flex flex-col gap-4">
                      {/* Dashboard Title row */}
                      <div className="flex justify-between items-center mt-2">
                        <h3 className="font-display font-black text-2xl text-white tracking-tight">DASHBOARD</h3>
                        <span className="flex items-center gap-1 bg-white/5 border border-white/10 px-3 py-1 rounded-full text-[10px] font-extrabold text-amber-400">
                          🔥 {streakCount} DAY STREAK
                        </span>
                      </div>

                      {/* Calorie Progress Ring display */}
                      <div className="w-full bg-white/5 border border-white/10 p-6 rounded-3xl flex flex-col items-center gap-3 relative overflow-hidden backdrop-blur-md">
                        <div className="w-36 h-36 rounded-full border-[8px] border-white/5 flex flex-col items-center justify-center relative">
                          <div className="absolute inset-0 rounded-full border-[8px] border-amber-400 border-t-transparent animate-pulse" />
                          <span className="font-display font-black text-2xl text-white">2671 kcal</span>
                          <span className="text-[10px] text-slate-400 font-semibold mt-0.5">Remaining</span>
                          <span className="text-[9px] text-amber-400 font-extrabold mt-1">● 0/140g Pro</span>
                        </div>
                      </div>

                      {/* Mission Checklist */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-4 backdrop-blur-md">
                        <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">TODAY'S MISSION CHECKLIST</h4>
                        
                        <div className="flex flex-col gap-3.5">
                          {/* Item 1 */}
                          <div className="flex justify-between items-center text-xs">
                            <div>
                              <p className="font-bold text-white">Hit Daily Calories Target</p>
                              <p className="text-[10px] text-slate-400 mt-0.5">0 / 2671 kcal</p>
                            </div>
                            <span className="text-base">⏳</span>
                          </div>

                          {/* Item 2 */}
                          <div className="flex justify-between items-center text-xs">
                            <div>
                              <p className="font-bold text-white">Reach Protein Target</p>
                              <p className="text-[10px] text-slate-400 mt-0.5">0 / 140 g</p>
                            </div>
                            <span className="text-base">⏳</span>
                          </div>

                          {/* Item 3 */}
                          <div className="flex justify-between items-center text-xs">
                            <div>
                              <p className="font-bold text-white">Complete Workout Split</p>
                              <p className="text-[10px] text-slate-400 mt-0.5">{workoutCompleted ? "✅ Completed" : "Action Required"}</p>
                            </div>
                            {workoutCompleted ? (
                              <span className="w-5 h-5 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 flex items-center justify-center text-[10px] font-bold">✓</span>
                            ) : (
                              <button
                                onClick={() => setWorkoutCompleted(true)}
                                className="bg-white/10 hover:bg-white/20 text-white border border-white/10 px-3 py-1.5 rounded-xl text-[10px] font-bold cursor-pointer transition-all"
                              >
                                Log Done
                              </button>
                            )}
                          </div>

                          {/* Item 4 */}
                          <div className="flex justify-between items-center text-xs">
                            <div>
                              <p className="font-bold text-white">Hydration Tracker</p>
                              <p className="text-[10px] text-slate-400 mt-0.5">{waterGlasses} / {waterTarget} glasses</p>
                            </div>
                            <div className="flex items-center gap-1.5">
                              <button
                                onClick={() => setWaterGlasses(g => g + 1)}
                                className="bg-amber-400 text-black px-2 py-1 rounded-lg text-[10px] font-extrabold cursor-pointer"
                              >
                                + Glass
                              </button>
                              <button
                                onClick={() => setWaterGlasses(0)}
                                className="bg-white/5 border border-white/10 text-slate-300 px-2 py-1 rounded-lg text-[10px] font-bold cursor-pointer"
                              >
                                Reset
                              </button>
                            </div>
                          </div>
                        </div>
                      </div>

                      {/* Echo Coach Advice card */}
                      <div className="bg-emerald-950/20 border border-emerald-500/10 p-5 rounded-3xl flex gap-3 items-start text-left backdrop-blur-md">
                        <div className="w-8 h-8 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 flex items-center justify-center text-sm font-black shrink-0">
                          E
                        </div>
                        <div>
                          <h4 className="font-bold text-xs text-emerald-400">Echo AI Coach</h4>
                          <p className="text-[10px] text-slate-300 leading-relaxed mt-1 font-semibold">
                            "Welcome to a brand new day! Ready to crush your fitness goals? Start by logging your first healthy meal using the Diet planner!"
                          </p>
                        </div>
                      </div>
                    </div>
                  )}

                  {/* ============================================================= */}
                  {/* TAB: DIET                                                     */}
                  {/* ============================================================= */}
                  {activeTab === 'DIET' && (
                    <div className="flex flex-col gap-4">
                      {/* Diet Title row */}
                      <div className="flex justify-between items-center mt-2">
                        <div>
                          <h3 className="font-display font-black text-2xl text-white tracking-tight">DIET PLANNER</h3>
                          <p className="text-[9px] text-slate-400 font-semibold mt-0.5">Type: Vegetarian | Budget: Low</p>
                        </div>
                        
                        <div className="flex items-center gap-1.5">
                          <button
                            onClick={triggerCameraScan}
                            disabled={isAnalyzingImage}
                            className="w-8 h-8 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 flex items-center justify-center cursor-pointer transition-all disabled:opacity-50"
                          >
                            <Camera className="w-4.5 h-4.5" />
                          </button>
                          <button
                            onClick={() => setIsManualAddOpen(true)}
                            className="w-8 h-8 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 flex items-center justify-center cursor-pointer transition-all"
                          >
                            <Plus className="w-4.5 h-4.5" />
                          </button>
                        </div>
                      </div>

                      {/* Vision Loader */}
                      {isAnalyzingImage && (
                        <div className="p-4 bg-emerald-500/10 border border-emerald-500/20 rounded-2xl flex items-center justify-center gap-3">
                          <span className="w-4.5 h-4.5 rounded-full border-2 border-emerald-400 border-t-transparent animate-spin" />
                          <span className="text-[10px] font-bold text-emerald-400">Analyzing portion calories...</span>
                        </div>
                      )}

                      {/* Daily nutrition budget card */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-4 backdrop-blur-md">
                        <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">DAILY NUTRITION BUDGETS</h4>
                        
                        <div className="flex flex-col gap-3 text-[11px]">
                          {[
                            { name: 'Calories', current: mealsLogged.reduce((acc, m) => acc + m.calories, 0), target: dietGoal.calories, unit: 'kcal' },
                            { name: 'Protein', current: mealsLogged.reduce((acc, m) => acc + m.protein, 0), target: dietGoal.protein, unit: 'g' },
                            { name: 'Carbs', current: mealsLogged.reduce((acc, m) => acc + m.carbs, 0), target: dietGoal.carbs, unit: 'g' },
                            { name: 'Fats', current: mealsLogged.reduce((acc, m) => acc + m.fats, 0), target: dietGoal.fats, unit: 'g' }
                          ].map((macro, idx) => (
                            <div key={idx} className="flex flex-col gap-1.5">
                              <div className="flex justify-between font-bold text-slate-300">
                                <span>{macro.name}</span>
                                <span>{macro.current} / {macro.target} {macro.unit}</span>
                              </div>
                              <div className="w-full bg-white/5 border border-white/5 h-2 rounded-full overflow-hidden">
                                <div
                                  className="bg-amber-400 h-full rounded-full transition-all"
                                  style={{ width: `${Math.min((macro.current / macro.target) * 100, 100)}%` }}
                                />
                              </div>
                            </div>
                          ))}
                        </div>
                      </div>

                      {/* 3D Illustration block */}
                      <div className="w-full h-24 rounded-3xl bg-slate-900 border border-white/5 relative overflow-hidden flex items-center justify-center shadow-inner">
                        <img
                          src="https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500&q=80"
                          alt="Healthy Meal"
                          className="absolute inset-0 w-full h-full object-cover opacity-30"
                        />
                        <div className="relative z-10 text-center">
                          <p className="text-xs font-black tracking-widest text-white uppercase">EchoFit Food Engine</p>
                          <p className="text-[9px] text-slate-400 font-semibold mt-1">Computer vision scanner active</p>
                        </div>
                      </div>

                      {/* Suggested meal schedule */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md">
                        <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">SUGGESTED MEAL SCHEDULE</h4>
                        
                        <div className="flex flex-col gap-2.5">
                          {[
                            { name: 'Oats & Milk', time: '08:30 AM', cal: 360, pro: 12 },
                            { name: 'Paneer Salad', time: '01:30 PM', cal: 310, pro: 18 },
                            { name: 'Soya Chunks fry', time: '05:00 PM', cal: 260, pro: 14 }
                          ].map((meal, idx) => (
                            <div key={idx} className="flex justify-between items-center p-3 bg-white/5 border border-white/5 rounded-2xl hover:border-white/10 transition-all text-xs">
                              <div>
                                <p className="font-bold text-white">{meal.name}</p>
                                <p className="text-[10px] text-slate-400 mt-0.5">{meal.time} | {meal.cal} kcal | {meal.pro}g Pro</p>
                              </div>
                              <button
                                onClick={() => handleLogMeal(meal.name, '1 serving', meal.cal, meal.pro)}
                                className="w-6 h-6 rounded-full bg-emerald-500/20 text-emerald-400 border border-emerald-500/30 flex items-center justify-center cursor-pointer font-bold text-sm"
                              >
                                +
                              </button>
                            </div>
                          ))}
                        </div>
                      </div>

                      {/* Logged Meals List */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md mb-2">
                        <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">LOGGED MEALS TODAY</h4>
                        
                        <div className="flex flex-col gap-2">
                          {mealsLogged.length === 0 ? (
                            <p className="text-[10px] text-slate-400 text-center py-4">No meals logged today yet.</p>
                          ) : (
                            mealsLogged.map(m => (
                              <div key={m.id} className="flex justify-between items-center p-3 bg-white/5 border border-white/5 rounded-2xl text-xs">
                                <div>
                                  <p className="font-bold text-white">{m.name}</p>
                                  <p className="text-[10px] text-slate-400 mt-0.5">{m.calories} kcal | {m.protein}g Pro | {m.carbs}g C | {m.fats}g F</p>
                                </div>
                                <button
                                  onClick={() => setMealsLogged(prev => prev.filter(item => item.id !== m.id))}
                                  className="text-slate-400 hover:text-red-500 transition-colors p-1"
                                >
                                  <Trash2 className="w-3.5 h-3.5" />
                                </button>
                              </div>
                            ))
                          )}
                        </div>
                      </div>
                    </div>
                  )}

                  {/* ============================================================= */}
                  {/* TAB: WORKOUT                                                  */}
                  {/* ============================================================= */}
                  {activeTab === 'WORKOUT' && (
                    <div className="flex flex-col gap-4">
                      {/* Title */}
                      <div className="flex flex-col gap-1 mt-2">
                        <h3 className="font-display font-black text-2xl text-white tracking-tight">WORKOUT PLAN</h3>
                        <p className="text-[9px] text-slate-400 font-semibold">Goal: Muscle Gain | Day 164</p>
                      </div>

                      {/* Tab Selector */}
                      <div className="flex bg-white/5 border border-white/10 p-1 rounded-2xl">
                        {['PLAN', 'VIDEO STUDIO'].map(subTab => (
                          <button
                            key={subTab}
                            onClick={() => setWorkoutSubTab(subTab)}
                            className={`flex-1 py-2 rounded-xl text-[10px] font-extrabold tracking-wider transition-all cursor-pointer ${
                              workoutSubTab === subTab
                                ? 'bg-amber-400 text-black shadow-md shadow-amber-500/10'
                                : 'text-slate-400 hover:text-white'
                            }`}
                          >
                            {subTab}
                          </button>
                        ))}
                      </div>

                      {workoutSubTab === 'PLAN' ? (
                        <div className="flex flex-col gap-4">
                          {/* Routine overview */}
                          <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-4 backdrop-blur-md">
                            <h4 className="font-display font-black text-base text-white tracking-tight">Push A - Hypertrophy</h4>
                            <p className="text-[10px] text-slate-400 font-semibold mt-[-8px]">Focus: Chest, Shoulders, Triceps</p>
                            
                            <div className="flex flex-col gap-3 text-xs mt-2">
                              {[
                                { name: 'Shoulder Arm Swings', desc: 'Warmup • 2 sets' },
                                { name: 'Incline Dumbbell Press', desc: '4 sets x 10-12 reps' },
                                { name: 'Standing Overhead Press', desc: '3 sets x 8-10 reps' },
                                { name: 'Lateral Raises', desc: '3 sets x 12-15 reps' },
                                { name: 'Triceps Cable Extensions', desc: '4 sets x 12 reps' }
                              ].map((exercise, idx) => (
                                <div key={idx} className="flex justify-between items-center py-2 border-b border-white/5 last:border-0">
                                  <div>
                                    <p className="font-bold text-white">{exercise.name}</p>
                                    <p className="text-[10px] text-slate-400 mt-0.5">{exercise.desc}</p>
                                  </div>
                                  <span className="w-2.5 h-2.5 rounded-full bg-amber-400" />
                                </div>
                              ))}
                            </div>
                          </div>

                          <button
                            onClick={() => {
                              setWorkoutCompleted(true)
                              alert("Workout session marked complete locally!")
                            }}
                            className="w-full py-4 bg-amber-400 hover:bg-amber-500 text-black font-black rounded-3xl text-xs cursor-pointer shadow-xl shadow-amber-500/10 transition-all uppercase tracking-wider"
                          >
                            Mark Workout Completed
                          </button>
                        </div>
                      ) : (
                        <div className="flex flex-col gap-4">
                          {/* Yoga list */}
                          <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md">
                            <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">FLEXIBILITY FLOW YOGA</h4>
                            <div className="flex flex-col gap-2.5">
                              {[
                                { name: 'Morning Flexibility Flow', mins: 15, cals: 80 },
                                { name: 'Back Pain Relief Yoga', mins: 10, cals: 60 }
                              ].map((yoga, idx) => (
                                <div
                                  key={idx}
                                  onClick={() => {
                                    setActiveVideoSession(yoga)
                                    setVideoTimer(yoga.mins * 60)
                                    setVideoIsPlaying(false)
                                  }}
                                  className="flex justify-between items-center p-3 bg-white/5 border border-white/5 rounded-2xl hover:border-white/10 transition-all text-xs cursor-pointer"
                                >
                                  <div>
                                    <p className="font-bold text-white">{yoga.name}</p>
                                    <p className="text-[10px] text-slate-400 mt-0.5">{yoga.mins} mins • Burn: {yoga.cals} kcal</p>
                                  </div>
                                  <Play className="w-3.5 h-3.5 text-slate-400" />
                                </div>
                              ))}
                            </div>
                          </div>

                          {/* Zumba list */}
                          <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md mb-2">
                            <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">CARDIO ZUMBA DANCE</h4>
                            <div className="flex flex-col gap-2.5">
                              {[
                                { name: 'Beginner Zumba Beat Dance', mins: 15, cals: 160 },
                                { name: 'Latin Salsa Cardio Blast', mins: 20, cals: 220 }
                              ].map((zumba, idx) => (
                                <div
                                  key={idx}
                                  onClick={() => {
                                    setActiveVideoSession(zumba)
                                    setVideoTimer(zumba.mins * 60)
                                    setVideoIsPlaying(false)
                                  }}
                                  className="flex justify-between items-center p-3 bg-white/5 border border-white/5 rounded-2xl hover:border-white/10 transition-all text-xs cursor-pointer"
                                >
                                  <div>
                                    <p className="font-bold text-white">{zumba.name}</p>
                                    <p className="text-[10px] text-slate-400 mt-0.5">{zumba.mins} mins • Burn: {zumba.cals} kcal</p>
                                  </div>
                                  <Play className="w-3.5 h-3.5 text-slate-400" />
                                </div>
                              ))}
                            </div>
                          </div>
                        </div>
                      )}
                    </div>
                  )}

                  {/* ============================================================= */}
                  {/* TAB: PROGRESS                                                 */}
                  {/* ============================================================= */}
                  {activeTab === 'PROGRESS' && (
                    <div className="flex flex-col gap-4">
                      {/* Title */}
                      <div className="flex justify-between items-center mt-2">
                        <div>
                          <h3 className="font-display font-black text-2xl text-white tracking-tight">PROGRESS</h3>
                          <p className="text-[9px] text-slate-400 font-semibold">Track weight, attendance, and photos</p>
                        </div>
                        <Lock className="w-4 h-4 text-amber-400" />
                      </div>

                      {/* Log weight panel */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md">
                        <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">LOG DAILY WEIGHT</h4>
                        <div className="flex items-center gap-2">
                          <input
                            type="number"
                            placeholder="Weight in kg"
                            value={newWeightInput}
                            onChange={e => setNewWeightInput(e.target.value)}
                            className="bg-white/5 border border-white/10 px-4 py-2.5 rounded-2xl text-xs focus:outline-none flex-1 text-white"
                          />
                          <button
                            onClick={() => {
                              if (!newWeightInput) return
                              const wt = parseFloat(newWeightInput)
                              setProgressWeight(newWeightInput)
                              setWeightLogs(prev => [...prev, { date: 'New', weight: wt }])
                              setNewWeightInput('')
                            }}
                            className="bg-amber-400 text-black px-4 py-2.5 rounded-2xl text-xs font-black cursor-pointer shadow-lg shadow-amber-500/10"
                          >
                            +
                          </button>
                        </div>
                        <p className="text-[10px] text-slate-300 font-bold mt-1">Current Weight: {progressWeight} kg | Goal: 70.0 kg</p>
                      </div>

                      {/* Weight Trend Chart */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-4 backdrop-blur-md">
                        <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">WEEKLY WEIGHT TREND</h4>
                        
                        <div className="w-full h-24 bg-black/40 border border-white/5 rounded-2xl p-2.5">
                          <svg viewBox="0 0 400 100" className="w-full h-full overflow-visible">
                            <path
                              d="M 20 80 Q 100 50 180 60 T 340 30"
                              fill="none"
                              stroke="#fbbf24"
                              strokeWidth="3.5"
                              strokeLinecap="round"
                            />
                            {weightLogs.map((log, idx) => {
                              const x = 20 + idx * 80
                              const y = 80 - (idx * 10)
                              return (
                                <g key={idx}>
                                  <circle cx={x} cy={y} r="5" fill="#fbbf24" stroke="white" strokeWidth="1.5" />
                                  <text x={x} y={y - 12} fontSize="8" fill="white" fontWeight="bold" textAnchor="middle">{log.weight}kg</text>
                                </g>
                              )
                            })}
                          </svg>
                        </div>
                      </div>

                      {/* Gamification Vault */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md">
                        <div className="flex justify-between items-center">
                          <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">GAMIFICATION VAULT</h4>
                          <span className="text-[9px] text-amber-300 font-black">Level 3 - Gym Warrior</span>
                        </div>
                        <div className="w-full bg-white/5 border border-white/5 h-2 rounded-full overflow-hidden">
                          <div className="bg-amber-400 h-full rounded-full w-[65%]" />
                        </div>
                        
                        <div className="flex gap-2 text-[9px] font-bold mt-1 text-center justify-between">
                          <span className="flex-1 bg-white/5 border border-white/5 p-2 rounded-xl text-white">💪 Pro Master</span>
                          <span className="flex-1 bg-white/5 border border-white/5 p-2 rounded-xl text-white">💧 Water Hero</span>
                          <span className="flex-1 bg-white/5 border border-white/5 p-2 rounded-xl text-white">🏆 Consistency</span>
                        </div>
                      </div>

                      {/* AI Weekly Report */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md">
                        <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">ECHO'S WEEKLY ASSESSMENT</h4>
                        <p className="text-[10px] text-slate-300 leading-relaxed font-semibold">
                          • Weight change: Down 1.3 kg from start.<br/>
                          • Hydration pacing: 92% targets locked.<br/>
                          • Routine compliance: 85% consistency.
                        </p>
                      </div>

                      {/* Progress Photo Checkins */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md mb-2">
                        <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase font-sans">PROGRESS PHOTO CHECKINS</h4>
                        
                        <div className="grid grid-cols-3 gap-2.5">
                          {['Front', 'Side', 'Back'].map(pos => {
                            const hasPhoto = photosList.find(p => p.position === pos)
                            const isUploading = isUploadingPhoto === pos
                            return (
                              <div
                                key={pos}
                                onClick={() => !isUploading && handlePhotoUpload(pos)}
                                className="aspect-[3/4] bg-white/5 border border-dashed border-white/10 rounded-2xl flex flex-col items-center justify-center gap-1 cursor-pointer hover:border-white/20 transition-all relative overflow-hidden"
                              >
                                {isUploading ? (
                                  <span className="w-4 h-4 rounded-full border border-amber-400 border-t-transparent animate-spin" />
                                ) : hasPhoto ? (
                                  <img src={hasPhoto.url} className="w-full h-full object-cover" alt={pos} />
                                ) : (
                                  <>
                                    <Camera className="w-4 h-4 text-slate-500" />
                                    <span className="text-[9px] text-slate-500 font-bold">{pos}</span>
                                  </>
                                )}
                              </div>
                            )
                          })}
                        </div>

                        <button
                          onClick={handleAnalyzePhysique}
                          disabled={photosList.length < 2 || isAnalyzingPhysique}
                          className="w-full py-3 bg-white/10 hover:bg-white/20 text-white border border-white/10 font-bold rounded-2xl text-[10px] cursor-pointer disabled:opacity-40 transition-all uppercase tracking-wider mt-2"
                        >
                          {isAnalyzingPhysique ? "Comparing visual frames..." : "Analyze Physique Transformation"}
                        </button>

                        {physiqueReport && (
                          <div className="p-3 bg-white/5 border border-white/5 rounded-2xl text-[10px] leading-relaxed text-slate-300 font-semibold mt-2">
                            {physiqueReport.split('\n').map((line, i) => (
                              <p key={i} className="mb-1">{line}</p>
                            ))}
                          </div>
                        )}
                      </div>
                    </div>
                  )}

                  {/* ============================================================= */}
                  {/* TAB: ECHOAI (AI Chat)                                         */}
                  {/* ============================================================= */}
                  {activeTab === 'ECHOAI' && (
                    <div className="flex flex-col gap-3 min-h-full flex-1 overflow-hidden">
                      {/* Title */}
                      <div className="flex items-center gap-3 border-b border-white/5 pb-3 mt-2 shrink-0">
                        <div className="w-8 h-8 rounded-full bg-gradient-to-br from-amber-400 to-amber-500 text-black flex items-center justify-center text-sm font-black shadow-lg">
                          E
                        </div>
                        <div>
                          <h3 className="font-bold text-xs text-white">Coach Echo</h3>
                          <p className="text-[9px] text-emerald-500 font-extrabold uppercase tracking-widest mt-0.5">● AI ONLINE</p>
                        </div>
                      </div>

                      {/* Chat Thread */}
                      <div className="flex-1 overflow-y-auto flex flex-col gap-3.5 pr-1 py-1 max-h-[480px]">
                        {chatMessages.map((msg, idx) => (
                          <div
                            key={idx}
                            className={`flex flex-col max-w-[85%] ${
                              msg.sender === 'user' ? 'ml-auto items-end' : 'mr-auto items-start'
                            }`}
                          >
                            <div
                              className={`p-3 rounded-2xl text-[11px] leading-relaxed border ${
                                msg.sender === 'user'
                                  ? 'bg-amber-400 text-black border-amber-400 rounded-tr-none'
                                  : 'bg-white/5 text-slate-200 border-white/10 rounded-tl-none'
                              }`}
                            >
                              {msg.text}
                            </div>
                          </div>
                        ))}
                        {isTyping && (
                          <div className="mr-auto items-start max-w-[85%] flex items-center gap-1 p-3 bg-white/5 border border-white/10 rounded-2xl rounded-tl-none">
                            <span className="w-1 h-1 rounded-full bg-slate-400 animate-bounce" />
                            <span className="w-1 h-1 rounded-full bg-slate-400 animate-bounce [animation-delay:0.2s]" />
                            <span className="w-1 h-1 rounded-full bg-slate-400 animate-bounce [animation-delay:0.4s]" />
                          </div>
                        )}
                      </div>

                      {/* Input row */}
                      <form onSubmit={handleSendMessage} className="flex gap-1.5 pt-2 border-t border-white/5 shrink-0 mt-auto">
                        <input
                          type="text"
                          placeholder="Type fitness queries..."
                          value={userInput}
                          onChange={e => setUserInput(e.target.value)}
                          className="flex-1 bg-white/5 border border-white/10 px-4 py-3 rounded-2xl text-[11px] focus:outline-none text-white placeholder-slate-500"
                        />
                        <button
                          type="submit"
                          className="bg-amber-400 hover:bg-amber-500 text-black p-3.5 rounded-2xl cursor-pointer transition-all shadow-md shrink-0"
                        >
                          <Send className="w-3.5 h-3.5" />
                        </button>
                      </form>
                    </div>
                  )}

                  {/* ============================================================= */}
                  {/* TAB: SETTING                                                  */}
                  {/* ============================================================= */}
                  {activeTab === 'SETTING' && (
                    <div className="flex flex-col gap-4">
                      {/* Title */}
                      <div className="flex flex-col gap-1 mt-2">
                        <h3 className="font-display font-black text-2xl text-white tracking-tight">SETTINGS</h3>
                        <p className="text-[9px] text-slate-400 font-semibold">Manage your reminders, keys, and data logs</p>
                      </div>

                      {/* WorkManager Reminders Card */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md">
                        <div className="flex items-center gap-2">
                          <Bell className="w-4 h-4 text-amber-400" />
                          <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">WORKMANAGER REMINDERS</h4>
                        </div>
                        
                        <div className="flex flex-col gap-3">
                          {reminders.map(rem => (
                            <div key={rem.id} className="flex justify-between items-center text-xs">
                              <div>
                                <p className="font-bold text-white">{rem.name}</p>
                                <p className="text-[10px] text-slate-400 mt-0.5">Scheduled at {rem.time}</p>
                              </div>
                              
                              <button
                                onClick={() => setReminders(prev => prev.map(r => r.id === rem.id ? { ...r, isEnabled: !r.isEnabled } : r))}
                                className={`w-8 h-4.5 rounded-full p-0.5 transition-colors cursor-pointer relative flex items-center ${
                                  rem.isEnabled ? 'bg-emerald-500' : 'bg-white/10'
                                }`}
                              >
                                <span
                                  className={`w-3.5 h-3.5 rounded-full bg-white transition-transform ${
                                    rem.isEnabled ? 'translate-x-3.5' : 'translate-x-0'
                                  }`}
                                />
                              </button>
                            </div>
                          ))}
                        </div>
                      </div>

                      {/* Cloud Sync Database Card */}
                      <div className="bg-white/5 border border-white/10 p-5 rounded-3xl flex flex-col gap-3.5 backdrop-blur-md">
                        <div className="flex items-center gap-2">
                          <CloudLightning className="w-4 h-4 text-amber-400" />
                          <h4 className="font-bold text-[11px] text-amber-400 tracking-wider uppercase">FIREBASE DATABASE SYNC</h4>
                        </div>
                        
                        <p className="text-[10px] text-slate-300 leading-relaxed font-semibold">
                          Automatically synchronizes local Room DB datasets with secure Cloud Firestore backends.
                        </p>

                        <button
                          onClick={handleForceSync}
                          disabled={isSyncingCloud}
                          className="w-full py-3 bg-gradient-to-r from-amber-400 to-amber-500 hover:from-amber-500 hover:to-amber-600 text-black font-black rounded-2xl text-[10px] cursor-pointer shadow-lg shadow-amber-500/10 transition-all uppercase tracking-wider flex items-center justify-center gap-2"
                        >
                          {isSyncingCloud ? (
                            <>
                              <RefreshCw className="w-3.5 h-3.5 animate-spin" />
                              UPLOADING TO FIRESTORE...
                            </>
                          ) : (
                            "FORCE FIREBASE SYNC"
                          )}
                        </button>
                      </div>

                      {/* Disclaimer Card */}
                      <div className="bg-rose-950/20 border border-rose-500/15 p-5 rounded-3xl flex gap-3 items-start text-left backdrop-blur-md mb-2">
                        <Info className="w-4.5 h-4.5 text-rose-400 shrink-0 mt-0.5" />
                        <div>
                          <h4 className="font-bold text-[11px] text-rose-400 tracking-wider uppercase">AI MEDICAL DISCLAIMER</h4>
                          <p className="text-[9px] text-slate-300 leading-relaxed mt-1 font-semibold">
                            AI diet plan generation, caloric vision scanning, and physical posture checks are calculated estimates and must not replace professional clinical therapy or customized medical diagnostics. Use responsibly.
                          </p>
                        </div>
                      </div>
                    </div>
                  )}
                </motion.div>
              </AnimatePresence>

            </div>

            {/* Bottom Navigation Bar */}
            <div className="h-[68px] border-t border-white/5 bg-[#0C0B12] px-2 flex justify-around items-center shrink-0 z-40 relative">
              {[
                { name: 'HOME', icon: Zap },
                { name: 'DIET', icon: Apple },
                { name: 'WORKOUT', icon: Dumbbell },
                { name: 'PROGRESS', icon: TrendingUp },
                { name: 'ECHOAI', icon: MessageSquare },
                { name: 'SETTING', icon: Settings }
              ].map(tab => {
                const IconComp = tab.icon
                const isActive = activeTab === tab.name
                return (
                  <button
                    key={tab.name}
                    onClick={() => {
                      setActiveTab(tab.name)
                      // Dismiss physique/analysis popups
                      setPhysiqueReport(null)
                    }}
                    className={`flex flex-col items-center gap-1 py-1 cursor-pointer transition-colors ${
                      isActive ? 'text-amber-400' : 'text-slate-500 hover:text-slate-300'
                    }`}
                  >
                    <IconComp className="w-4.5 h-4.5" />
                    <span className="text-[7.5px] font-black tracking-wider uppercase">{tab.name}</span>
                  </button>
                )
              })}
            </div>

          </div>
        </div>

      </div>

      {/* Manual Meal Add Modal Representation inside Phone Shell */}
      {isManualAddOpen && (
        <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-[#12111A] border border-white/10 p-6 rounded-3xl max-w-[340px] w-full shadow-2xl flex flex-col gap-4 text-left">
            <div className="flex justify-between items-center border-b border-white/5 pb-2">
              <h3 className="font-display font-black text-sm text-white tracking-tight">ADD MEAL MANUALLY</h3>
              <button onClick={() => setIsManualAddOpen(false)} className="text-slate-400 hover:text-white font-extrabold text-xs">✕</button>
            </div>
            
            <form onSubmit={handleManualMealSubmit} className="flex flex-col gap-3 text-xs">
              <div className="flex flex-col gap-1.5">
                <label className="font-bold text-slate-400">Meal Name</label>
                <input
                  type="text"
                  placeholder="e.g. Boiled chicken with rice"
                  value={manualFoodName}
                  onChange={e => setManualFoodName(e.target.value)}
                  className="bg-white/5 border border-white/10 px-3 py-2 rounded-xl text-white focus:outline-none"
                  required
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div className="flex flex-col gap-1.5">
                  <label className="font-bold text-slate-400">Calories (kcal)</label>
                  <input
                    type="number"
                    placeholder="350"
                    value={manualCals}
                    onChange={e => setManualCals(e.target.value)}
                    className="bg-white/5 border border-white/10 px-3 py-2 rounded-xl text-white focus:outline-none"
                  />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className="font-bold text-slate-400">Protein (g)</label>
                  <input
                    type="number"
                    placeholder="25"
                    value={manualPro}
                    onChange={e => setManualPro(e.target.value)}
                    className="bg-white/5 border border-white/10 px-3 py-2 rounded-xl text-white focus:outline-none"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div className="flex flex-col gap-1.5">
                  <label className="font-bold text-slate-400">Carbs (g)</label>
                  <input
                    type="number"
                    placeholder="45"
                    value={manualCarbs}
                    onChange={e => setManualCarbs(e.target.value)}
                    className="bg-white/5 border border-white/10 px-3 py-2 rounded-xl text-white focus:outline-none"
                  />
                </div>
                <div className="flex flex-col gap-1.5">
                  <label className="font-bold text-slate-400">Fats (g)</label>
                  <input
                    type="number"
                    placeholder="10"
                    value={manualFats}
                    onChange={e => setManualFats(e.target.value)}
                    className="bg-white/5 border border-white/10 px-3 py-2 rounded-xl text-white focus:outline-none"
                  />
                </div>
              </div>

              <button
                type="submit"
                className="w-full py-3 bg-amber-400 hover:bg-amber-500 text-black font-black rounded-xl cursor-pointer mt-2 shadow-lg shadow-amber-500/10 text-center uppercase tracking-wider"
              >
                Save Meal Log
              </button>
            </form>
          </div>
        </div>
      )}

      {/* Simulated Yoga/Zumba Video Streaming Modal */}
      {activeVideoSession && (
        <div className="fixed inset-0 z-50 bg-black/70 backdrop-blur-md flex items-center justify-center p-4">
          <div className="bg-[#12111A] border border-white/10 p-5 rounded-3xl max-w-[340px] w-full shadow-2xl flex flex-col gap-4 text-left">
            <div className="flex justify-between items-center border-b border-white/5 pb-2">
              <h3 className="font-bold text-xs text-white truncate max-w-[200px]">{activeVideoSession.name}</h3>
              <button
                onClick={() => {
                  setActiveVideoSession(null)
                  setVideoIsPlaying(false)
                }}
                className="text-slate-400 hover:text-white font-extrabold text-xs"
              >
                ✕
              </button>
            </div>

            {/* Video Screen representation */}
            <div className="w-full h-36 rounded-2xl bg-slate-950 flex flex-col items-center justify-center relative overflow-hidden border border-white/5 shadow-inner">
              {videoIsPlaying ? (
                <div className="text-center relative z-10">
                  <span className="w-2.5 h-2.5 bg-red-500 rounded-full inline-block animate-ping mr-1.5" />
                  <span className="text-[9px] font-black text-white tracking-widest uppercase">STREAMING SESSION...</span>
                </div>
              ) : (
                <div className="text-center p-4 relative z-10 cursor-pointer" onClick={() => setVideoIsPlaying(true)}>
                  <Play className="w-8 h-8 text-white mx-auto opacity-70 mb-1.5 hover:opacity-100 transition-opacity" />
                  <p className="text-[9px] text-slate-400">Press play to start stream.</p>
                </div>
              )}
              <div className="absolute bottom-2 left-2 bg-black/60 px-2 py-0.5 rounded text-[8px] font-extrabold text-white tracking-widest">
                {videoIsPlaying ? 'LIVE STREAM' : 'PAUSED'}
              </div>
            </div>

            {/* Progress Bar */}
            <div className="flex flex-col gap-1 text-[10px] text-slate-400 font-bold">
              <div className="flex justify-between">
                <span>Timer Progress</span>
                <span>{Math.floor(videoTimer / 60)}:{(videoTimer % 60).toString().padStart(2, '0')} left</span>
              </div>
              <div className="w-full bg-white/5 border border-white/5 h-1.5 rounded-full overflow-hidden">
                <div
                  className="bg-amber-400 h-full rounded-full transition-all"
                  style={{ width: `${((activeVideoSession.mins * 60 - videoTimer) / (activeVideoSession.mins * 60)) * 100}%` }}
                />
              </div>
            </div>

            {/* Controls */}
            <div className="flex gap-2 text-[10px] font-extrabold mt-1">
              <button
                onClick={() => setVideoIsPlaying(!videoIsPlaying)}
                className="flex-1 py-3 bg-white/5 hover:bg-white/10 text-white border border-white/10 rounded-xl cursor-pointer"
              >
                {videoIsPlaying ? 'PAUSE' : 'PLAY'}
              </button>
              <button
                onClick={() => {
                  setWorkoutCompleted(true)
                  setActiveVideoSession(null)
                  setVideoIsPlaying(false)
                }}
                className="flex-1 py-3 bg-amber-400 text-black rounded-xl cursor-pointer"
              >
                MARK COMPLETE
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  )
}

export default App
