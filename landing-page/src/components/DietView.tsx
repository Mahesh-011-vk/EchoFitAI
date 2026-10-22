import { useState } from 'react'
import {
  Apple,
  Plus,
  Camera,
  Trash2,
  Sparkles,
  Utensils,
  RefreshCw
} from 'lucide-react'
import type { GoalAnalysis, MealItem, UserProfile } from '../types/fitness'
import { HEALTHY_FOOD_PRESETS } from '../utils/aiEngine'

interface DietViewProps {
  userProfile: UserProfile
  goalAnalysis: GoalAnalysis
  mealsLogged: MealItem[]
  onAddMeal: (meal: Omit<MealItem, 'id'>) => void
  onRemoveMeal: (mealId: string) => void
  onOpenManualModal: () => void
}

export const DietView: React.FC<DietViewProps> = ({
  userProfile,
  goalAnalysis,
  mealsLogged,
  onAddMeal,
  onRemoveMeal,
  onOpenManualModal
}) => {
  const [isScanningFood, setIsScanningFood] = useState(false)
  const [scanResult, setScanResult] = useState<string | null>(null)
  const [activeCategoryFilter, setActiveCategoryFilter] = useState<'All' | 'Breakfast' | 'Lunch' | 'Snacks' | 'Dinner'>('All')

  // Caloric calculations
  const totalCalories = mealsLogged.reduce((sum, m) => sum + m.calories, 0)
  const totalProtein = mealsLogged.reduce((sum, m) => sum + m.protein, 0)
  const totalCarbs = mealsLogged.reduce((sum, m) => sum + m.carbs, 0)
  const totalFats = mealsLogged.reduce((sum, m) => sum + m.fats, 0)

  const remainingCalories = Math.max(0, goalAnalysis.calorieTarget - totalCalories)

  // Simulate AI camera meal vision scan
  const handleSimulateVisionScan = () => {
    setIsScanningFood(true)
    setScanResult(null)
    setTimeout(() => {
      setIsScanningFood(false)
      const scannedMeal = {
        name: userProfile.dietType.includes('Veg')
          ? 'Paneer Tikka with Sprouted Dal Bowl'
          : 'Grilled Lemon Herb Chicken with Brown Rice',
        portion: '1 Balanced Meal Bowl (350g)',
        calories: 460,
        protein: 36,
        carbs: 45,
        fats: 14,
        category: 'Lunch' as const
      }
      onAddMeal(scannedMeal)
      setScanResult(`Scanned & Logged: "${scannedMeal.name}" (+460 kcal, 36g Protein)`)
      setTimeout(() => setScanResult(null), 5000)
    }, 2000)
  }

  const mealCategories: Array<'All' | 'Breakfast' | 'Lunch' | 'Snacks' | 'Dinner'> = ['All', 'Breakfast', 'Lunch', 'Snacks', 'Dinner']

  const filteredMeals = activeCategoryFilter === 'All'
    ? mealsLogged
    : mealsLogged.filter(m => m.category === activeCategoryFilter)

  return (
    <div className="flex flex-col gap-6 w-full max-w-7xl mx-auto pb-12 animate-in fade-in duration-300">
      
      {/* Header Row */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 classic-card p-6 rounded-3xl shadow-sm">
        <div>
          <div className="flex items-center gap-2">
            <span className="text-[10px] font-extrabold text-[#721C28] bg-[#721C28]/10 border border-[#721C28]/25 px-3 py-0.5 rounded-full uppercase tracking-wider">
              Diet Type: {userProfile.dietType}
            </span>
            <span className="text-xs text-[#7A6E6B]">● Budget: {userProfile.budgetLevel}</span>
          </div>
          <h1 className="font-serif font-black text-2xl sm:text-3xl text-[#231815] tracking-tight mt-1">
            Nutrition & Macro Planner
          </h1>
          <p className="text-[#5C4F4D] text-xs sm:text-sm mt-0.5">
            Calibrated for {userProfile.fitnessGoal} ({goalAnalysis.calorieTarget} kcal target)
          </p>
        </div>

        <div className="flex items-center gap-3">
          {/* AI Vision Scan Button */}
          <button
            onClick={handleSimulateVisionScan}
            disabled={isScanningFood}
            className="flex items-center gap-2 bg-[#FAF7F2] hover:bg-white border border-[#E5DDD0] text-[#231815] font-extrabold px-4 py-2.5 rounded-full text-xs cursor-pointer shadow-sm transition-all disabled:opacity-50"
          >
            {isScanningFood ? (
              <>
                <RefreshCw className="w-4 h-4 animate-spin text-[#721C28]" />
                <span className="text-[#721C28]">AI ANALYZING MEAL...</span>
              </>
            ) : (
              <>
                <Camera className="w-4 h-4 text-[#721C28]" />
                <span>AI PHOTO FOOD SCAN</span>
              </>
            )}
          </button>

          {/* Add Food Manually */}
          <button
            onClick={onOpenManualModal}
            className="flex items-center gap-1.5 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white font-extrabold px-5 py-2.5 rounded-full text-xs cursor-pointer shadow-md shadow-[#721C28]/20 transition-all uppercase tracking-wider"
          >
            <Plus className="w-4 h-4" />
            <span>LOG CUSTOM MEAL</span>
          </button>
        </div>
      </div>

      {/* Vision Scan Notification feedback */}
      {scanResult && (
        <div className="p-4 bg-[#721C28]/10 border border-[#721C28]/25 rounded-2xl text-[#721C28] text-xs font-bold flex items-center gap-2 animate-in slide-in-from-top">
          <Sparkles className="w-4 h-4 text-[#721C28] shrink-0" />
          <span>{scanResult}</span>
        </div>
      )}

      {/* Macro Targets Summary Card */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">Calories Logged</span>
          <div className="flex items-baseline gap-1">
            <span className="text-xl font-serif font-black text-[#231815]">{totalCalories}</span>
            <span className="text-xs text-[#8A7D79]">/ {goalAnalysis.calorieTarget}</span>
          </div>
          <span className="text-[10px] text-[#721C28] font-bold">{remainingCalories} kcal remaining</span>
        </div>

        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#721C28] uppercase">Protein Target</span>
          <div className="flex items-baseline gap-1">
            <span className="text-xl font-serif font-black text-[#231815]">{totalProtein}g</span>
            <span className="text-xs text-[#8A7D79]">/ {goalAnalysis.proteinTarget}g</span>
          </div>
          <div className="w-full bg-[#EFE9DF] h-1.5 rounded-full overflow-hidden mt-1">
            <div className="bg-[#721C28] h-full rounded-full" style={{ width: `${Math.min(100, (totalProtein / goalAnalysis.proteinTarget) * 100)}%` }} />
          </div>
        </div>

        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#0D9488] uppercase">Carbohydrates</span>
          <div className="flex items-baseline gap-1">
            <span className="text-xl font-serif font-black text-[#231815]">{totalCarbs}g</span>
            <span className="text-xs text-[#8A7D79]">/ {goalAnalysis.carbsTarget}g</span>
          </div>
          <div className="w-full bg-[#EFE9DF] h-1.5 rounded-full overflow-hidden mt-1">
            <div className="bg-[#0D9488] h-full rounded-full" style={{ width: `${Math.min(100, (totalCarbs / goalAnalysis.carbsTarget) * 100)}%` }} />
          </div>
        </div>

        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#9D174D] uppercase">Healthy Fats</span>
          <div className="flex items-baseline gap-1">
            <span className="text-xl font-serif font-black text-[#231815]">{totalFats}g</span>
            <span className="text-xs text-[#8A7D79]">/ {goalAnalysis.fatsTarget}g</span>
          </div>
          <div className="w-full bg-[#EFE9DF] h-1.5 rounded-full overflow-hidden mt-1">
            <div className="bg-[#9D174D] h-full rounded-full" style={{ width: `${Math.min(100, (totalFats / goalAnalysis.fatsTarget) * 100)}%` }} />
          </div>
        </div>
      </div>

      {/* LIQUID FROSTED GLASS MEAL CATEGORY FILTER TABS (MATCHING USER'S IMAGE) */}
      <div className="flex items-center justify-start overflow-x-auto pb-1">
        <div className="p-1 rounded-full liquid-glass-capsule shadow-md inline-flex items-center gap-1">
          {mealCategories.map(cat => {
            const isCatActive = activeCategoryFilter === cat
            return (
              <button
                key={cat}
                onClick={() => setActiveCategoryFilter(cat)}
                className={`px-5 py-2 rounded-full text-xs transition-all cursor-pointer ${
                  isCatActive
                    ? 'liquid-tab-active shadow-md text-[#721C28] font-black'
                    : 'liquid-tab-idle font-semibold'
                }`}
              >
                {cat}
              </button>
            )
          })}
        </div>
      </div>

      {/* Main Grid: Daily Meals Logged & Wholesome Budget Library */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        
        {/* Left Column (lg:col-span-8): Today's Meal Timeline */}
        <div className="lg:col-span-8 classic-card p-6 rounded-3xl flex flex-col gap-5 shadow-sm">
          <div className="flex justify-between items-center">
            <h2 className="font-serif font-black text-xl text-[#231815] tracking-tight flex items-center gap-2">
              <Utensils className="w-5 h-5 text-[#721C28]" /> Today's Logged Meals ({filteredMeals.length})
            </h2>
            <span className="text-xs font-bold text-[#7A6E6B]">Total: {totalCalories} kcal</span>
          </div>

          {filteredMeals.length === 0 ? (
            <div className="py-12 flex flex-col items-center justify-center text-center gap-3 bg-[#FAF7F2] rounded-2xl border border-dashed border-[#E5DDD0]">
              <Apple className="w-10 h-10 text-[#A59995]" />
              <p className="text-sm font-bold text-[#5C4F4D]">No meals logged in this category yet.</p>
              <p className="text-xs text-[#8A7D79]">Use the quick presets on the right or the AI Food Scanner above!</p>
            </div>
          ) : (
            <div className="flex flex-col gap-3">
              {filteredMeals.map(meal => (
                <div
                  key={meal.id}
                  className="p-4 bg-[#FAF7F2] hover:bg-white border border-[#E5DDD0] rounded-2xl flex flex-col sm:flex-row sm:items-center justify-between gap-3 transition-colors shadow-sm"
                >
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="text-[10px] font-black uppercase text-[#721C28] bg-[#721C28]/10 border border-[#721C28]/20 px-2.5 py-0.5 rounded-full">
                        {meal.category}
                      </span>
                      <h3 className="font-serif font-bold text-sm text-[#231815]">{meal.name}</h3>
                    </div>
                    <p className="text-xs text-[#7A6E6B] mt-0.5">Portion: {meal.portion}</p>
                  </div>

                  <div className="flex items-center justify-between sm:justify-end gap-4">
                    <div className="flex items-center gap-3 text-xs font-bold">
                      <span className="text-[#231815] font-black">{meal.calories} kcal</span>
                      <span className="text-[#721C28]">{meal.protein}g P</span>
                      <span className="text-[#0D9488]">{meal.carbs}g C</span>
                      <span className="text-[#9D174D]">{meal.fats}g F</span>
                    </div>

                    <button
                      onClick={() => onRemoveMeal(meal.id)}
                      className="p-1.5 text-[#A59995] hover:text-rose-600 rounded-lg hover:bg-white transition-colors cursor-pointer"
                      title="Remove Meal"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Right Column (lg:col-span-4): Budget-Friendly Foods Library */}
        <div className="lg:col-span-4 classic-card p-6 rounded-3xl flex flex-col gap-4 shadow-sm">
          <div>
            <h2 className="font-serif font-black text-lg text-[#231815] tracking-tight flex items-center gap-2">
              <Sparkles className="w-4 h-4 text-[#721C28]" /> Budget Healthy Foods
            </h2>
            <p className="text-[11px] text-[#7A6E6B] font-semibold mt-0.5">
              High-protein, budget-friendly choices from EchoFit Engine
            </p>
          </div>

          <div className="flex flex-col gap-2.5 max-h-[500px] overflow-y-auto pr-1">
            {HEALTHY_FOOD_PRESETS.map((food, i) => (
              <div
                key={i}
                className="p-3 bg-[#FAF7F2] border border-[#E5DDD0] rounded-2xl flex items-center justify-between gap-2 hover:border-[#721C28]/40 transition-all shadow-sm"
              >
                <div className="text-left">
                  <h4 className="text-xs font-serif font-bold text-[#231815] truncate max-w-[180px]">{food.name}</h4>
                  <p className="text-[10px] text-[#7A6E6B]">{food.portion} ● <span className="text-[#721C28] font-black">{food.protein}g Pro</span> ({food.calories} kcal)</p>
                </div>

                <button
                  onClick={() => onAddMeal(food)}
                  className="bg-white hover:bg-[#721C28] hover:text-white text-[#231815] border border-[#E5DDD0] px-3 py-1.5 rounded-full text-xs font-black cursor-pointer transition-all shrink-0 flex items-center gap-1 shadow-sm"
                >
                  <Plus className="w-3.5 h-3.5" />
                  <span>Add</span>
                </button>
              </div>
            ))}
          </div>
        </div>

      </div>

    </div>
  )
}
