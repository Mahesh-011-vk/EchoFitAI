import { useState } from 'react'
import {
  Settings,
  Dumbbell,
  Plus,
  Trash2,
  Edit2,
  CheckCircle2,
  Save
} from 'lucide-react'
import type { UserProfile, GoalAnalysis, SavedRoutine } from '../types/fitness'
import { DEFAULT_ROUTINES } from '../utils/aiEngine'

interface AccountViewProps {
  userProfile: UserProfile
  setUserProfile: (profile: UserProfile) => void
  goalAnalysis: GoalAnalysis
  savedRoutines: SavedRoutine[]
  setSavedRoutines: React.Dispatch<React.SetStateAction<SavedRoutine[]>>
  activeRoutine: SavedRoutine
  setActiveRoutine: (routine: SavedRoutine) => void
  onOpenCreateRoutineModal: () => void
}

export const AccountView: React.FC<AccountViewProps> = ({
  userProfile,
  setUserProfile,
  goalAnalysis,
  savedRoutines,
  setSavedRoutines,
  activeRoutine,
  setActiveRoutine,
  onOpenCreateRoutineModal
}) => {
  const [isEditingProfile, setIsEditingProfile] = useState(false)
  const [editForm, setEditForm] = useState<UserProfile>(userProfile)
  const [saveSuccess, setSaveSuccess] = useState(false)

  const handleProfileSave = (e: React.FormEvent) => {
    e.preventDefault()
    setUserProfile(editForm)
    setIsEditingProfile(false)
    setSaveSuccess(true)
    setTimeout(() => setSaveSuccess(false), 3000)
  }

  const handleDeleteRoutine = (routineId: string) => {
    if (savedRoutines.length <= 1) {
      alert("You must keep at least one workout routine stored.")
      return
    }
    const updated = savedRoutines.filter(r => r.id !== routineId)
    setSavedRoutines(updated)
    if (activeRoutine.id === routineId) {
      setActiveRoutine(updated[0])
    }
  }

  const handleResetDefaultRoutines = () => {
    if (confirm("Reset to default EchoFit workout routines? Any custom routines will be refreshed.")) {
      setSavedRoutines(DEFAULT_ROUTINES)
      setActiveRoutine(DEFAULT_ROUTINES[0])
    }
  }

  return (
    <div className="flex flex-col gap-8 w-full max-w-7xl mx-auto pb-12 animate-in fade-in duration-300">
      
      {/* Top Header Card */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 classic-card p-6 rounded-3xl shadow-sm">
        <div className="flex items-center gap-4">
          <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-[#721C28] via-[#8C2434] to-[#4E101B] flex items-center justify-center text-white font-serif font-black text-2xl shadow-xl shadow-[#721C28]/25">
            {userProfile.name ? userProfile.name[0].toUpperCase() : 'U'}
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="font-serif font-black text-2xl sm:text-3xl text-[#231815] tracking-tight">
                {userProfile.name}
              </h1>
              <span className="text-[10px] bg-emerald-500/15 text-emerald-800 border border-emerald-500/25 px-3 py-0.5 rounded-full font-bold uppercase">
                Pro Account
              </span>
            </div>
            <p className="text-xs text-[#5C4F4D] mt-1">
              {userProfile.fitnessGoal} ● {userProfile.experienceLevel} Level ● {userProfile.dietType} Diet
            </p>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <button
            onClick={() => {
              setEditForm(userProfile)
              setIsEditingProfile(!isEditingProfile)
            }}
            className="flex items-center gap-2 bg-[#FAF7F2] hover:bg-white text-[#231815] px-4 py-2.5 rounded-full text-xs font-bold border border-[#E5DDD0] cursor-pointer transition-all shadow-sm"
          >
            <Edit2 className="w-3.5 h-3.5 text-[#721C28]" />
            <span>{isEditingProfile ? 'Cancel Editing' : 'Edit Physical Profile'}</span>
          </button>
        </div>
      </div>

      {saveSuccess && (
        <div className="p-4 bg-emerald-500/15 border border-emerald-500/25 rounded-2xl text-emerald-800 text-xs font-bold flex items-center gap-2 animate-in slide-in-from-top">
          <CheckCircle2 className="w-4 h-4 text-emerald-700" />
          <span>Profile updated and fitness targets recalculated successfully!</span>
        </div>
      )}

      {/* Edit Profile Form (Expanded when clicking Edit) */}
      {isEditingProfile && (
        <div className="classic-card border-[#721C28]/30 p-6 rounded-3xl shadow-md animate-in slide-in-from-top duration-300">
          <div className="flex justify-between items-center mb-6 border-b border-[#E8E1D5] pb-4">
            <div>
              <h2 className="font-serif font-black text-xl text-[#231815] tracking-tight flex items-center gap-2">
                <Settings className="w-5 h-5 text-[#721C28]" /> Personal Fitness Attributes
              </h2>
              <p className="text-xs text-[#7A6E6B]">Recalculates your Harris-Benedict BMR, calorie budget, and macros automatically.</p>
            </div>
          </div>

          <form onSubmit={handleProfileSave} className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 text-xs">
            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Full Name</label>
              <input
                type="text"
                value={editForm.name}
                onChange={e => setEditForm({ ...editForm, name: e.target.value })}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
                required
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Age</label>
              <input
                type="number"
                value={editForm.age}
                onChange={e => setEditForm({ ...editForm, age: parseInt(e.target.value) || 25 })}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
                required
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Gender</label>
              <select
                value={editForm.gender}
                onChange={e => setEditForm({ ...editForm, gender: e.target.value as any })}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              >
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Height (cm)</label>
              <input
                type="number"
                value={editForm.height}
                onChange={e => setEditForm({ ...editForm, height: parseFloat(e.target.value) || 175 })}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
                required
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Current Weight (kg)</label>
              <input
                type="number"
                step="0.1"
                value={editForm.weight}
                onChange={e => setEditForm({ ...editForm, weight: parseFloat(e.target.value) || 70 })}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
                required
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Target Weight (kg)</label>
              <input
                type="number"
                step="0.1"
                value={editForm.targetWeight}
                onChange={e => setEditForm({ ...editForm, targetWeight: parseFloat(e.target.value) || 70 })}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
                required
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Primary Fitness Goal</label>
              <select
                value={editForm.fitnessGoal}
                onChange={e => setEditForm({ ...editForm, fitnessGoal: e.target.value as any })}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              >
                <option value="Fat Loss">Fat Loss (Caloric Deficit)</option>
                <option value="Muscle Gain">Muscle Gain (Hypertrophy Surplus)</option>
                <option value="Maintenance">Maintenance (Body Recomposition)</option>
                <option value="Endurance">Cardio Endurance & V02 Max</option>
              </select>
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Experience Level</label>
              <select
                value={editForm.experienceLevel}
                onChange={e => setEditForm({ ...editForm, experienceLevel: e.target.value as any })}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              >
                <option value="Beginner">Beginner (0 - 6 months)</option>
                <option value="Intermediate">Intermediate (6 months - 2 years)</option>
                <option value="Advanced">Advanced (2+ years dedicated)</option>
              </select>
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Dietary Type</label>
              <select
                value={editForm.dietType}
                onChange={e => setEditForm({ ...editForm, dietType: e.target.value as any })}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              >
                <option value="Vegetarian">Vegetarian</option>
                <option value="Non-Vegetarian">Non-Vegetarian</option>
                <option value="Eggetarian">Eggetarian</option>
                <option value="Vegan">Vegan</option>
              </select>
            </div>

            <div className="col-span-full flex justify-end gap-3 pt-4 border-t border-[#E8E1D5]">
              <button
                type="button"
                onClick={() => setIsEditingProfile(false)}
                className="px-4 py-2 bg-[#FAF7F2] hover:bg-white text-[#3D3130] rounded-full font-bold cursor-pointer border border-[#E5DDD0]"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-6 py-2.5 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white font-black rounded-full cursor-pointer shadow-lg shadow-[#721C28]/20 uppercase tracking-wider flex items-center gap-1.5"
              >
                <Save className="w-4 h-4" />
                <span>Save Profile Updates</span>
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Calculated Physical Metrics Summary */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">BMI & Classification</span>
          <span className="font-serif font-black text-xl text-[#231815]">{goalAnalysis.bmi}</span>
          <span className="text-[10px] text-[#721C28] font-bold">{goalAnalysis.bmiCategory}</span>
        </div>

        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">Body Fat %</span>
          <span className="font-serif font-black text-xl text-[#721C28]">{goalAnalysis.estimatedBodyFat}%</span>
          <span className="text-[10px] text-[#7A6E6B] font-semibold">Visual Est.</span>
        </div>

        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">Daily Calorie Target</span>
          <span className="font-serif font-black text-xl text-[#231815]">{goalAnalysis.calorieTarget}</span>
          <span className="text-[10px] text-emerald-700 font-bold">kcal / day</span>
        </div>

        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">Protein Target</span>
          <span className="font-serif font-black text-xl text-[#231815]">{goalAnalysis.proteinTarget}g</span>
          <span className="text-[10px] text-[#721C28] font-bold">Optimal synthesis</span>
        </div>

        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">Hydration Goal</span>
          <span className="font-serif font-black text-xl text-cyan-800">{goalAnalysis.waterIntakeTargetLiters} L</span>
          <span className="text-[10px] text-[#7A6E6B] font-semibold">{(goalAnalysis.waterIntakeTargetLiters * 4).toFixed(0)} Glasses</span>
        </div>

        <div className="p-4 classic-card rounded-2xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">Echo Fitness Score</span>
          <span className="font-serif font-black text-xl text-emerald-700">{goalAnalysis.fitnessScore}</span>
          <span className="text-[10px] text-[#7A6E6B] font-semibold">out of 100</span>
        </div>
      </div>

      {/* ========================================================================= */}
      {/* PERSONALIZED ROUTINE STORAGE SYSTEM ("store his routines")                 */}
      {/* ========================================================================= */}
      <div className="classic-card p-6 rounded-3xl flex flex-col gap-6 shadow-sm">
        
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <div className="flex items-center gap-2">
              <Dumbbell className="w-5 h-5 text-[#721C28]" />
              <h2 className="font-serif font-black text-2xl text-[#231815] tracking-tight">
                Stored Workout Routines ({savedRoutines.length})
              </h2>
            </div>
            <p className="text-xs text-[#5C4F4D] mt-1">
              Store custom multi-day splits, configure sets and reps, or switch active workout programs anytime.
            </p>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={handleResetDefaultRoutines}
              className="text-xs font-bold text-[#7A6E6B] hover:text-[#231815] px-3.5 py-2 rounded-full border border-[#E5DDD0] bg-[#FAF7F2] cursor-pointer transition-all shadow-sm"
              title="Restore Default AI Programs"
            >
              Reset Defaults
            </button>
            <button
              onClick={onOpenCreateRoutineModal}
              className="flex items-center gap-2 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white font-black px-5 py-2.5 rounded-full text-xs cursor-pointer shadow-lg shadow-[#721C28]/20 transition-all uppercase tracking-wider"
            >
              <Plus className="w-4 h-4" />
              <span>CREATE CUSTOM ROUTINE</span>
            </button>
          </div>
        </div>

        {/* Stored Routines Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {savedRoutines.map(routine => {
            const isActive = activeRoutine.id === routine.id
            const totalExercises = routine.splits.reduce((acc, s) => acc + s.exercises.length, 0)

            return (
              <div
                key={routine.id}
                className={`p-6 rounded-3xl border transition-all flex flex-col justify-between gap-5 relative overflow-hidden shadow-sm ${
                  isActive
                    ? 'bg-[#FAF7F2] border-[#721C28]/50 shadow-md ring-1 ring-[#721C28]/30'
                    : 'classic-card hover:border-[#721C28]/30'
                }`}
              >
                <div>
                  <div className="flex justify-between items-center mb-3">
                    <span className="text-[10px] font-extrabold text-[#721C28] bg-[#721C28]/10 border border-[#721C28]/25 px-3 py-0.5 rounded-full uppercase tracking-wider">
                      {routine.focus}
                    </span>
                    {isActive ? (
                      <span className="bg-emerald-500/15 text-emerald-800 border border-emerald-500/25 text-[10px] font-black px-3 py-0.5 rounded-full flex items-center gap-1">
                        ✓ ACTIVE ROUTINE
                      </span>
                    ) : (
                      <span className="text-[10px] text-[#8A7D79] font-bold">
                        {routine.isCustom ? 'Custom Routine' : 'AI Preset'}
                      </span>
                    )}
                  </div>

                  <h3 className="font-serif font-black text-xl text-[#231815] leading-tight">
                    {routine.name}
                  </h3>
                  <p className="text-xs text-[#5C4F4D] mt-1 leading-relaxed">
                    {routine.description}
                  </p>
                </div>

                {/* Split Days details */}
                <div className="bg-[#FAF7F2] border border-[#E5DDD0] rounded-2xl p-3 flex flex-col gap-1.5 text-xs">
                  <div className="flex justify-between text-[#7A6E6B] font-bold text-[11px]">
                    <span>Training Days:</span>
                    <span className="text-[#231815]">{routine.daysPerWeek} Days / Week</span>
                  </div>
                  <div className="flex justify-between text-[#7A6E6B] font-bold text-[11px]">
                    <span>Total Exercises:</span>
                    <span className="text-[#231815]">{totalExercises} Exercises</span>
                  </div>
                  <div className="mt-1 flex flex-wrap gap-1">
                    {routine.splits.map((s, idx) => (
                      <span key={idx} className="bg-white border border-[#E5DDD0] px-2 py-0.5 rounded-md text-[9px] text-[#5C4F4D] font-semibold">
                        Day {idx + 1}: {s.name.split('(')[0]}
                      </span>
                    ))}
                  </div>
                </div>

                {/* Actions */}
                <div className="flex items-center gap-2 pt-2 border-t border-[#E8E1D5]">
                  {!isActive ? (
                    <button
                      onClick={() => setActiveRoutine(routine)}
                      className="flex-1 py-2.5 bg-[#721C28] hover:bg-[#50111A] text-white font-black rounded-full text-xs cursor-pointer shadow-md shadow-[#721C28]/15 transition-all uppercase tracking-wider"
                    >
                      ACTIVATE ROUTINE
                    </button>
                  ) : (
                    <div className="flex-1 py-2.5 bg-[#E8F5E9] text-emerald-800 font-black rounded-full text-xs text-center border border-emerald-500/20">
                      CURRENTLY ACTIVE
                    </div>
                  )}

                  {routine.isCustom && (
                    <button
                      onClick={() => handleDeleteRoutine(routine.id)}
                      className="p-2.5 bg-[#FAF7F2] hover:bg-rose-50 text-[#7A6E6B] hover:text-rose-700 border border-[#E5DDD0] rounded-full cursor-pointer transition-colors"
                      title="Delete Custom Routine"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  )}
                </div>

              </div>
            )
          })}
        </div>

      </div>

    </div>
  )
}
