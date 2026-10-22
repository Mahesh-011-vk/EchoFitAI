import { useState, useRef, useEffect } from 'react'
import {
  Send,
  RotateCcw
} from 'lucide-react'
import type { ChatMessage, UserProfile, GoalAnalysis, SavedRoutine, MealItem } from '../types/fitness'

interface CoachChatViewProps {
  userProfile: UserProfile
  goalAnalysis: GoalAnalysis
  activeRoutine: SavedRoutine
  mealsLogged: MealItem[]
  waterGlasses: number
}

export const CoachChatView: React.FC<CoachChatViewProps> = ({
  userProfile,
  goalAnalysis,
  activeRoutine,
  mealsLogged,
  waterGlasses
}) => {
  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      id: '1',
      sender: 'ai',
      text: `Greetings ${userProfile.name}. I am Coach Echo, your private algorithmic athletic director. I am tracking your ${userProfile.fitnessGoal} protocol (${goalAnalysis.calorieTarget} kcal target, ${goalAnalysis.proteinTarget}g protein). Inquire about exercise substitutions, progressive overload, or nutritional calibration anytime.`,
      timestamp: 'Just now'
    }
  ])
  const [input, setInput] = useState('')
  const [isTyping, setIsTyping] = useState(false)
  const chatBottomRef = useRef<HTMLDivElement>(null)

  const totalCalories = mealsLogged.reduce((sum, m) => sum + m.calories, 0)
  const totalProtein = mealsLogged.reduce((sum, m) => sum + m.protein, 0)

  useEffect(() => {
    chatBottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages, isTyping])

  const quickPrompts = [
    "How's my protein intake today?",
    "Suggest a substitute for Pull-Ups",
    "What's a high-protein budget snack?",
    "Tips for Incline Dumbbell Press form"
  ]

  const handleSend = (textToSend?: string) => {
    const text = textToSend || input
    if (!text.trim()) return

    const userMsg: ChatMessage = {
      id: Date.now().toString(),
      sender: 'user',
      text,
      timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    }

    setMessages(prev => [...prev, userMsg])
    if (!textToSend) setInput('')
    setIsTyping(true)

    // Generate intelligent contextual reply
    setTimeout(() => {
      setIsTyping(false)
      const lower = text.toLowerCase()
      let reply = `That is a commendable strategy, ${userProfile.name}. Maintain strict fidelity to your ${activeRoutine.name} split and uphold biomechanical control across all sets.`

      if (lower.includes('protein') || lower.includes('macros')) {
        reply = `You have logged ${totalProtein}g of your ${goalAnalysis.proteinTarget}g daily protein target (${Math.max(0, goalAnalysis.proteinTarget - totalProtein)}g remaining). To advance your ${userProfile.fitnessGoal} objectives, incorporate ${userProfile.dietType.includes('Veg') ? 'sprouted lentils, roasted chana, or whole paneer' : 'egg whites, lean chicken, or yellow lentils'} into your next feeding window.`
      } else if (lower.includes('pull-up') || lower.includes('substitute') || lower.includes('alternative')) {
        reply = `For Pull-Ups in your current split, optimal substitutes include:\n1. Cable Lat Pulldowns (4 sets of 10-12 reps with retracted scapulae).\n2. Unilateral Heavy Dumbbell Rows or Band-Assisted Pull-Ups. Both preserve crucial vertical and horizontal pulling vectors.`
      } else if (lower.includes('snack') || lower.includes('food') || lower.includes('budget') || lower.includes('diet')) {
        reply = `Top budget-friendly choices for your ${userProfile.dietType} profile:\n• Roasted Bengal Gram (Chana): 50g provides 11g clean protein with high mineral density.\n• Sprouted Moong salad seasoned with cold-pressed olive oil & lime.\n• Pan-seared paneer or eggs with antioxidant greens.`
      } else if (lower.includes('water') || lower.includes('hydration')) {
        reply = `You currently hold ${waterGlasses} glasses out of your target ${(goalAnalysis.waterIntakeTargetLiters * 4).toFixed(0)} glasses (~${goalAnalysis.waterIntakeTargetLiters} Liters). Adequate cellular hydration optimizes myofibrillar protein synthesis and autonomic HRV readiness.`
      } else if (lower.includes('form') || lower.includes('press') || lower.includes('squat')) {
        reply = `Form cue: Maintain neutral cervical alignment and create high intra-abdominal pressure. On incline dumbbell presses, select a 30° bench incline to maximize clavicular pectoralis tension over anterior deltoid dominance.`
      }

      const aiMsg: ChatMessage = {
        id: (Date.now() + 1).toString(),
        sender: 'ai',
        text: reply,
        timestamp: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
      }
      setMessages(prev => [...prev, aiMsg])
    }, 1100)
  }

  return (
    <div className="flex flex-col gap-6 w-full max-w-5xl mx-auto pb-12 animate-in fade-in duration-300">
      
      {/* Header */}
      <div className="flex justify-between items-center classic-card p-6 rounded-3xl shadow-sm">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-2xl bg-gradient-to-br from-[#721C28] to-[#4E101B] flex items-center justify-center text-white font-serif font-black text-xl shadow-lg shadow-[#721C28]/20">
            E
          </div>
          <div>
            <h1 className="font-serif font-black text-2xl text-[#231815] tracking-tight">Coach Echo AI</h1>
            <p className="text-xs text-[#721C28] font-bold flex items-center gap-1.5">
              <span className="w-2 h-2 rounded-full bg-[#721C28] animate-pulse" />
              On-Device Adaptive Fitness Model Active
            </p>
          </div>
        </div>

        <button
          onClick={() => setMessages([messages[0]])}
          className="text-xs font-bold text-[#7A6E6B] hover:text-[#231815] flex items-center gap-1 bg-[#FAF7F2] hover:bg-white px-3.5 py-2 rounded-full border border-[#E5DDD0] cursor-pointer transition-all shadow-sm"
        >
          <RotateCcw className="w-3.5 h-3.5" /> Clear Chat
        </button>
      </div>

      {/* Chat Area Card */}
      <div className="classic-card rounded-3xl p-6 flex flex-col h-[520px] shadow-sm">
        
        {/* Messages Stream */}
        <div className="flex-1 overflow-y-auto space-y-4 pr-2">
          {messages.map(msg => (
            <div
              key={msg.id}
              className={`flex items-start gap-3 ${msg.sender === 'user' ? 'flex-row-reverse' : ''}`}
            >
              <div className={`w-8 h-8 rounded-full flex items-center justify-center shrink-0 text-xs font-serif font-black ${
                msg.sender === 'user'
                  ? 'bg-[#721C28] text-white shadow-sm'
                  : 'bg-[#FAF7F2] text-[#721C28] border border-[#E5DDD0]'
              }`}>
                {msg.sender === 'user' ? 'U' : 'E'}
              </div>

              <div className={`max-w-[80%] rounded-2xl p-4 text-xs leading-relaxed whitespace-pre-line shadow-sm ${
                msg.sender === 'user'
                  ? 'bg-gradient-to-r from-[#721C28] to-[#8C2434] text-white font-medium'
                  : 'bg-[#FAF7F2] border border-[#E5DDD0] text-[#231815] font-medium'
              }`}>
                <p>{msg.text}</p>
                <span className={`text-[9px] block mt-1.5 ${msg.sender === 'user' ? 'text-white/70' : 'text-[#8A7D79]'}`}>
                  {msg.timestamp}
                </span>
              </div>
            </div>
          ))}

          {isTyping && (
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 rounded-full bg-[#FAF7F2] text-[#721C28] border border-[#E5DDD0] flex items-center justify-center shrink-0 text-xs font-serif font-black">
                E
              </div>
              <div className="bg-[#FAF7F2] border border-[#E5DDD0] text-[#7A6E6B] rounded-2xl px-4 py-3 text-xs flex items-center gap-1.5 shadow-sm">
                <span className="w-1.5 h-1.5 bg-[#721C28] rounded-full animate-bounce" />
                <span className="w-1.5 h-1.5 bg-[#721C28] rounded-full animate-bounce [animation-delay:0.2s]" />
                <span className="w-1.5 h-1.5 bg-[#721C28] rounded-full animate-bounce [animation-delay:0.4s]" />
              </div>
            </div>
          )}
          <div ref={chatBottomRef} />
        </div>

        {/* Quick Suggestion Chips */}
        <div className="flex items-center gap-2 overflow-x-auto py-3 scrollbar-none border-t border-[#E8E1D5] mt-3">
          {quickPrompts.map((q, i) => (
            <button
              key={i}
              onClick={() => handleSend(q)}
              className="bg-[#FAF7F2] hover:bg-white border border-[#E5DDD0] px-3.5 py-1.5 rounded-full text-[11px] text-[#5C4F4D] hover:text-[#721C28] whitespace-nowrap cursor-pointer transition-all shadow-sm"
            >
              💬 {q}
            </button>
          ))}
        </div>

        {/* Input Bar */}
        <form onSubmit={e => { e.preventDefault(); handleSend(); }} className="flex gap-2">
          <input
            type="text"
            placeholder="Ask Coach Echo about workouts, nutrition, sets, or recovery..."
            value={input}
            onChange={e => setInput(e.target.value)}
            className="flex-1 bg-[#FAF7F2] border border-[#E5DDD0] px-4 py-3 rounded-full text-xs text-[#231815] placeholder-[#8A7D79] focus:outline-none focus:border-[#721C28]"
          />
          <button
            type="submit"
            className="bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white px-6 py-3 rounded-full text-xs font-black cursor-pointer transition-all flex items-center gap-1.5 shadow-md shadow-[#721C28]/20"
          >
            <Send className="w-4 h-4" />
            <span className="hidden sm:inline">SEND</span>
          </button>
        </form>

      </div>

    </div>
  )
}
