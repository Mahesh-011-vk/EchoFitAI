import { useState } from 'react'
import { X, Plus, Trash2, Dumbbell } from 'lucide-react'
import type { SavedRoutine, WorkoutSplitDay, ExerciseItem } from '../types/fitness'

interface RoutineModalProps {
  isOpen: boolean
  onClose: () => void
  onSaveRoutine: (routine: SavedRoutine) => void
}

export const RoutineModal: React.FC<RoutineModalProps> = ({
  isOpen,
  onClose,
  onSaveRoutine
}) => {
  const [name, setName] = useState('')
  const [focus, setFocus] = useState('Hypertrophy')
  const [description, setDescription] = useState('')
  const [splits, setSplits] = useState<WorkoutSplitDay[]>([
    {
      dayIndex: 1,
      name: 'Day 1: Upper Body Strength',
      focus: 'Chest, Back & Shoulders',
      exercises: [
        {
          id: 'ex_1',
          name: 'Barbell Incline Bench Press',
          sets: 4,
          reps: '8-10',
          targetMuscle: 'Upper Chest',
          equipment: 'Barbell & Bench',
          instructions: ['Lower with control to clavicle', 'Press exploding upward without locking out elbows'],
          commonMistakes: ['Bouncing bar off sternum', 'Flaring elbows at 90 degrees'],
          alternative1: 'Incline Dumbbell Press',
          alternative2: 'Weighted Chest Dips'
        },
        {
          id: 'ex_2',
          name: 'Chest Supported T-Bar Row',
          sets: 4,
          reps: '10-12',
          targetMuscle: 'Upper Back / Lats',
          equipment: 'T-Bar Machine',
          instructions: ['Drive with elbows past ribcage', 'Hold squeeze at peak contraction for 1 sec'],
          commonMistakes: ['Using momentum to yank weight', 'Rounding thoracic spine'],
          alternative1: 'Barbell Bent Over Row',
          alternative2: 'Seated Cable Row'
        }
      ]
    },
    {
      dayIndex: 2,
      name: 'Day 2: Lower Body Power',
      focus: 'Quads, Hamstrings & Calves',
      exercises: [
        {
          id: 'ex_3',
          name: 'Barbell Back Squat',
          sets: 4,
          reps: '6-8',
          targetMuscle: 'Quadriceps / Glutes',
          equipment: 'Barbell & Squat Rack',
          instructions: ['Hit parallel or below depth', 'Brace core and drive through midfoot'],
          commonMistakes: ['Knees caving inward', 'Chest collapsing forward'],
          alternative1: 'Leg Press',
          alternative2: 'Bulgarian Split Squats'
        }
      ]
    }
  ])

  const [activeSplitIndex, setActiveSplitIndex] = useState(0)
  const [newExName, setNewExName] = useState('')
  const [newExSets, setNewExSets] = useState('3')
  const [newExReps, setNewExReps] = useState('10-12')

  if (!isOpen) return null

  const handleAddExerciseToSplit = () => {
    if (!newExName.trim()) return

    const newEx: ExerciseItem = {
      id: `custom_ex_${Date.now()}`,
      name: newExName.trim(),
      sets: parseInt(newExSets) || 3,
      reps: newExReps.trim() || '10-12',
      targetMuscle: 'Target Muscle Group',
      equipment: 'Gym Equipment',
      instructions: ['Control the eccentric negative', 'Explode on concentric contraction'],
      commonMistakes: ['Using excessive momentum'],
      alternative1: 'Dumbbell Alternative',
      alternative2: 'Cable Alternative'
    }

    setSplits(prev =>
      prev.map((s, idx) =>
        idx === activeSplitIndex
          ? { ...s, exercises: [...s.exercises, newEx] }
          : s
      )
    )

    setNewExName('')
    setNewExSets('3')
    setNewExReps('10-12')
  }

  const handleRemoveExercise = (splitIdx: number, exId: string) => {
    setSplits(prev =>
      prev.map((s, idx) =>
        idx === splitIdx
          ? { ...s, exercises: s.exercises.filter(e => e.id !== exId) }
          : s
      )
    )
  }

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault()
    if (!name.trim()) {
      alert("Please enter a routine name.")
      return
    }

    const newRoutine: SavedRoutine = {
      id: `custom_routine_${Date.now()}`,
      name: name.trim(),
      focus,
      description: description.trim() || 'Custom user crafted workout routine stored on EchoFit.',
      daysPerWeek: splits.length,
      isCustom: true,
      createdAt: new Date().toISOString().split('T')[0],
      splits
    }

    onSaveRoutine(newRoutine)
    onClose()
  }

  return (
    <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-md flex items-center justify-center p-4 overflow-y-auto">
      <div className="bg-[#FAF7F2] border border-[#E5DDD0] rounded-3xl max-w-3xl w-full p-6 sm:p-8 shadow-2xl flex flex-col gap-6 text-left my-8">
        
        {/* Header */}
        <div className="flex justify-between items-center border-b border-[#E8E1D5] pb-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-2xl bg-gradient-to-br from-[#721C28] to-[#4E101B] text-white flex items-center justify-center font-serif font-black shadow-lg shadow-[#721C28]/20">
              <Dumbbell className="w-5 h-5" />
            </div>
            <div>
              <h2 className="font-serif font-black text-xl text-[#231815]">Create Custom Routine</h2>
              <p className="text-xs text-[#7A6E6B]">Build your customized split and store it permanently in your account.</p>
            </div>
          </div>
          <button onClick={onClose} className="text-[#7A6E6B] hover:text-[#231815] p-2 rounded-full hover:bg-white cursor-pointer">
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSave} className="flex flex-col gap-6">
          
          {/* Metadata */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 text-xs">
            <div className="flex flex-col gap-1.5 sm:col-span-2">
              <label className="font-bold text-[#3D3130]">Routine Name</label>
              <input
                type="text"
                placeholder="e.g. 4-Day Upper / Lower Strength Builder"
                value={name}
                onChange={e => setName(e.target.value)}
                className="bg-white border border-[#E5DDD0] px-4 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
                required
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Training Focus</label>
              <select
                value={focus}
                onChange={e => setFocus(e.target.value)}
                className="bg-white border border-[#E5DDD0] px-4 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              >
                <option value="Hypertrophy">Hypertrophy (Muscle Gain)</option>
                <option value="Strength & Power">Strength & Power</option>
                <option value="Fat Loss & HIIT">Fat Loss & HIIT</option>
                <option value="Bodyweight Calisthenics">Bodyweight Calisthenics</option>
                <option value="General Conditioning">General Conditioning</option>
              </select>
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Description</label>
              <input
                type="text"
                placeholder="e.g. High intensity lifting focused on progressive compound sets."
                value={description}
                onChange={e => setDescription(e.target.value)}
                className="bg-white border border-[#E5DDD0] px-4 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              />
            </div>
          </div>

          {/* Split Day Tabs */}
          <div className="flex flex-col gap-3">
            <div className="flex justify-between items-center text-xs font-bold text-[#3D3130]">
              <span>Split Days ({splits.length} Days)</span>
              <span className="text-[11px] text-[#721C28] font-bold">Select day below to configure exercises</span>
            </div>

            <div className="flex items-center gap-2 overflow-x-auto pb-1 scrollbar-none">
              {splits.map((s, idx) => (
                <button
                  type="button"
                  key={s.dayIndex}
                  onClick={() => setActiveSplitIndex(idx)}
                  className={`px-4 py-2 rounded-full text-xs font-bold whitespace-nowrap cursor-pointer transition-all border shadow-sm ${
                    activeSplitIndex === idx
                      ? 'liquid-tab-active text-[#721C28]'
                      : 'bg-white text-[#5C4F4D] border-[#E5DDD0] hover:text-[#721C28]'
                  }`}
                >
                  Day {idx + 1}: {s.name} ({s.exercises.length} Ex)
                </button>
              ))}
            </div>
          </div>

          {/* Current Split Exercises List */}
          <div className="bg-white border border-[#E5DDD0] p-4 rounded-2xl flex flex-col gap-3 shadow-sm">
            <div className="flex justify-between items-center text-xs font-bold text-[#231815]">
              <span>Exercises for: {splits[activeSplitIndex]?.name}</span>
              <span className="text-[#7A6E6B]">{splits[activeSplitIndex]?.exercises.length} exercises configured</span>
            </div>

            <div className="flex flex-col gap-2 max-h-[180px] overflow-y-auto pr-1">
              {splits[activeSplitIndex]?.exercises.map(ex => (
                <div
                  key={ex.id}
                  className="p-3 bg-[#FAF7F2] border border-[#E5DDD0] rounded-xl flex items-center justify-between text-xs"
                >
                  <div>
                    <h4 className="font-serif font-bold text-[#231815]">{ex.name}</h4>
                    <p className="text-[10px] text-[#7A6E6B]">{ex.sets} Sets × {ex.reps} Reps | {ex.targetMuscle}</p>
                  </div>
                  <button
                    type="button"
                    onClick={() => handleRemoveExercise(activeSplitIndex, ex.id)}
                    className="text-[#A59995] hover:text-rose-700 p-1 cursor-pointer"
                  >
                    <Trash2 className="w-3.5 h-3.5" />
                  </button>
                </div>
              ))}
            </div>

            {/* Quick Add Exercise to Active Split */}
            <div className="pt-3 border-t border-[#E8E1D5] grid grid-cols-2 sm:grid-cols-5 gap-2 text-xs">
              <input
                type="text"
                placeholder="Exercise Name"
                value={newExName}
                onChange={e => setNewExName(e.target.value)}
                className="col-span-2 bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] text-xs focus:outline-none focus:border-[#721C28]"
              />
              <input
                type="number"
                placeholder="Sets"
                value={newExSets}
                onChange={e => setNewExSets(e.target.value)}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] text-xs focus:outline-none focus:border-[#721C28]"
              />
              <input
                type="text"
                placeholder="Reps (e.g. 10-12)"
                value={newExReps}
                onChange={e => setNewExReps(e.target.value)}
                className="bg-[#FAF7F2] border border-[#E5DDD0] px-3 py-2 rounded-xl text-[#231815] text-xs focus:outline-none focus:border-[#721C28]"
              />
              <button
                type="button"
                onClick={handleAddExerciseToSplit}
                className="bg-white hover:bg-[#721C28] hover:text-white text-[#721C28] font-bold px-3 py-2 rounded-xl text-xs cursor-pointer border border-[#E5DDD0] shadow-sm transition-colors"
              >
                + Add Ex
              </button>
            </div>
          </div>

          {/* Footer Submit */}
          <div className="flex justify-end gap-3 pt-4 border-t border-[#E8E1D5]">
            <button
              type="button"
              onClick={onClose}
              className="px-5 py-2.5 bg-white border border-[#E5DDD0] hover:bg-[#F3EFE6] text-[#3D3130] rounded-full text-xs font-bold cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-6 py-2.5 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white font-black rounded-full text-xs cursor-pointer shadow-lg shadow-[#721C28]/20 uppercase tracking-wider"
            >
              SAVE ROUTINE TO ACCOUNT
            </button>
          </div>

        </form>

      </div>
    </div>
  )
}
