import type { UserProfile, GoalAnalysis, SavedRoutine, CardioSession, MealItem, GuidedProgram, RecoveryMetrics, CalendarDayItem } from '../types/fitness'

/**
 * Calculates complete AI Goal Analysis replicating Android EchoAIEngine.kt
 */
export function analyzeGoal(profile: UserProfile): GoalAnalysis {
  const heightM = profile.height / 100
  const bmi = Math.round((profile.weight / (heightM * heightM)) * 10) / 10

  let bmiCategory = 'Normal Weight'
  if (bmi < 18.5) bmiCategory = 'Underweight'
  else if (bmi < 25.0) bmiCategory = 'Normal Weight'
  else if (bmi < 30.0) bmiCategory = 'Overweight'
  else bmiCategory = 'Obese'

  // Visual Body Fat estimation
  const baseFat = profile.gender === 'Male'
    ? (1.20 * bmi) + (0.23 * profile.age) - 16.2
    : (1.20 * bmi) + (0.23 * profile.age) - 5.4
  const estimatedBodyFat = Math.round(Math.min(50, Math.max(5, baseFat)) * 10) / 10

  // BMR (Harris-Benedict Equation)
  const bmr = profile.gender === 'Male'
    ? 88.362 + (13.397 * profile.weight) + (4.799 * profile.height) - (5.677 * profile.age)
    : 447.593 + (9.247 * profile.weight) + (3.098 * profile.height) - (4.330 * profile.age)

  // TDEE estimate (Moderate activity assumed for fitness seekers)
  const tdee = bmr * 1.375

  // Calorie and Macro targets based on fitness goal
  let calorieTarget = Math.round(tdee)
  if (profile.fitnessGoal === 'Fat Loss') {
    calorieTarget = Math.max(1200, Math.round(tdee - 500))
  } else if (profile.fitnessGoal === 'Muscle Gain') {
    calorieTarget = Math.round(tdee + 300)
  }

  // Protein Target (2.2g/kg for Fat Loss, 2.0g/kg for Muscle Gain, 1.6g/kg general)
  const proteinFactor = profile.fitnessGoal === 'Muscle Gain' ? 2.0 : profile.fitnessGoal === 'Fat Loss' ? 2.2 : 1.6
  const proteinTarget = Math.round(profile.weight * proteinFactor)

  const proteinKcal = proteinTarget * 4
  const fatKcal = Math.round(calorieTarget * 0.25)
  const fatsTarget = Math.max(40, Math.round(fatKcal / 9))

  const carbsKcal = calorieTarget - proteinKcal - fatKcal
  const carbsTarget = Math.max(100, Math.round(carbsKcal / 4))

  const fiberTarget = profile.gender === 'Male' ? 35 : 25
  const waterIntakeTargetLiters = Math.round((3.0 + (profile.weight > 80 ? 1.0 : 0) + (profile.fitnessGoal === 'Muscle Gain' ? 0.5 : 0)) * 10) / 10

  const suggestedWeightGoal = Math.round((22.0 * heightM * heightM) * 10) / 10

  let thirtyDayMission = ''
  switch (profile.fitnessGoal) {
    case 'Fat Loss':
      thirtyDayMission = `Mission Shred: Shed body fat while preserving lean muscle. Maintain a 500 kcal deficit, hit ${proteinTarget}g daily protein, and log 4 intense workout splits per week.`
      break
    case 'Muscle Gain':
      thirtyDayMission = `Mission Bulk: Build solid lean muscle mass. Consume a clean ${calorieTarget} kcal surplus, lift with progressive overload, and sleep at least 8 hours.`
      break
    case 'Body Recomposition':
      thirtyDayMission = `Mission Recomp: Strip fat and pack muscle simultaneously. Target ${proteinTarget}g protein, stay at maintenance calories (${calorieTarget} kcal), and focus on compound lifts.`
      break
    default:
      thirtyDayMission = `Mission Active: Build lifelong strength and cardiovascular endurance. Drink ${waterIntakeTargetLiters}L of water daily, hit 4 weekly workouts, and eat clean whole foods.`
  }

  let fitnessScore = 55
  if (bmi >= 18.5 && bmi <= 24.9) fitnessScore += 20
  if (profile.experienceLevel === 'Intermediate') fitnessScore += 10
  if (profile.experienceLevel === 'Advanced') fitnessScore += 15
  if (profile.injuriesOrLimitations === 'None') fitnessScore += 10
  fitnessScore = Math.min(100, Math.max(10, fitnessScore))

  return {
    bmi,
    bmiCategory,
    estimatedBodyFat,
    calorieTarget,
    proteinTarget,
    carbsTarget,
    fatsTarget,
    fiberTarget,
    waterIntakeTargetLiters,
    suggestedWeightGoal,
    thirtyDayMission,
    fitnessScore
  }
}

/**
 * Built-in default routines that match Android EchoAIEngine.kt
 */
export const DEFAULT_ROUTINES: SavedRoutine[] = [
  {
    id: 'routine-ppl',
    name: 'Echo Push-Pull-Legs Hypertrophy',
    description: 'Gold standard 4-day athletic hypertrophy routine for muscle building and strength progression.',
    focus: 'Hypertrophy & Strength',
    daysPerWeek: 4,
    isCustom: false,
    createdAt: '2026-01-01',
    splits: [
      {
        dayIndex: 1,
        name: 'Push Day (Chest, Shoulders & Triceps)',
        focus: 'Chest & Shoulder Hypertrophy',
        exercises: [
          {
            id: 'g1',
            name: 'Incline Dumbbell Press',
            sets: 4,
            reps: '8-12',
            targetMuscle: 'Upper Chest & Front Delts',
            equipment: 'Dumbbells, Incline Bench',
            instructions: ['Set bench to 30 degrees angle', 'Lower dumbbells slowly until thumbs touch upper chest line', 'Drive up smoothly without violently locking elbows at the top'],
            commonMistakes: ['Setting angle above 45 degrees loading front delts', 'Flaring elbows 90 degrees out'],
            alternative1: 'Flat Barbell Bench Press',
            alternative2: 'Incline Dumbbell Flyes'
          },
          {
            id: 'g2',
            name: 'Overhead Barbell Military Press',
            sets: 4,
            reps: '6-10',
            targetMuscle: 'Anterior & Lateral Deltoids',
            equipment: 'Barbell, Squat Rack',
            instructions: ['Rack bar at collarbone height', 'Brace core tightly and squeeze glutes', 'Press bar straight overhead moving head back and forward in rhythm'],
            commonMistakes: ['Arching lower back into pseudo-incline bench', 'Bouncing bar off collarbones'],
            alternative1: 'Seated Dumbbell Shoulder Press',
            alternative2: 'Handstand Pushups'
          },
          {
            id: 'g3',
            name: 'Dumbbell Lateral Raises',
            sets: 3,
            reps: '12-15',
            targetMuscle: 'Lateral Deltoids (Side Delts)',
            equipment: 'Dumbbells',
            instructions: ['Maintain slight 15-degree forward torso lean', 'Raise weights outward with elbows leading up to shoulder level', 'Control eccentric descent for 2 full seconds'],
            commonMistakes: ['Swinging torso violently using momentum', 'Shrugging traps up to ears'],
            alternative1: 'Cable Lateral Raises',
            alternative2: 'Resistance Band Lateral Raises'
          },
          {
            id: 'g4',
            name: 'Cable Triceps Rope Pushdowns',
            sets: 3,
            reps: '12-15',
            targetMuscle: 'Triceps (Lateral & Long Head)',
            equipment: 'Cable Machine, Rope Attachment',
            instructions: ['Pin elbows tight against rib cage', 'Extend arms downward and spread rope outwards at bottom', 'Squeeze triceps for 1 second peak hold'],
            commonMistakes: ['Letting elbows wander forward during negative', 'Leaning whole body over cable'],
            alternative1: 'Bench Dips',
            alternative2: 'Overhead Dumbbell Triceps Extensions'
          }
        ]
      },
      {
        dayIndex: 2,
        name: 'Pull Day (Back, Rear Delts & Biceps)',
        focus: 'Back Width & Arm Thickness',
        exercises: [
          {
            id: 'g5',
            name: 'Wide-Grip Bodyweight Pull-Ups',
            sets: 4,
            reps: '8-12',
            targetMuscle: 'Latissimus Dorsi & Upper Back',
            equipment: 'Pull-Up Bar',
            instructions: ['Grip bar slightly wider than shoulder width', 'Initiate by depressing scapulae and pulling collarbone to bar', 'Control descent to full dead-hang stretch'],
            commonMistakes: ['Kicking legs for kipping momentum', 'Only doing half-reps without chin over bar'],
            alternative1: 'Lat Pulldown Machine',
            alternative2: 'Resistance Band Assisted Pull-Ups'
          },
          {
            id: 'g6',
            name: 'Seated Cable Row',
            sets: 4,
            reps: '10-12',
            targetMuscle: 'Rhomboids, Mid-Back & Lats',
            equipment: 'Cable Machine, V-Bar',
            instructions: ['Keep torso tall with chest puffed out', 'Pull handle into navel driving elbows backward', 'Squeeze shoulder blades together firmly'],
            commonMistakes: ['Swinging torso back and forth like a rowing boat', 'Shrugging shoulders up'],
            alternative1: 'One-Arm Dumbbell Row',
            alternative2: 'Barbell Bent-Over Row'
          },
          {
            id: 'g7',
            name: 'Standing Barbell Biceps Curls',
            sets: 3,
            reps: '10-12',
            targetMuscle: 'Biceps Brachii',
            equipment: 'EZ Bar or Straight Barbell',
            instructions: ['Keep elbows tucked against sides', 'Curl bar up toward upper chest without swinging hips', 'Lower under strict 3-second tension'],
            commonMistakes: ['Throwing lower back backward to swing weight', 'Resting bar at top without tension'],
            alternative1: 'Incline Dumbbell Curls',
            alternative2: 'Hammer Curls'
          },
          {
            id: 'g17',
            name: 'Cable Face Pulls',
            sets: 3,
            reps: '15-20',
            targetMuscle: 'Rear Delts & Rotator Cuff',
            equipment: 'Cable Machine, Rope',
            instructions: ['Set pulley at eye level', 'Pull rope towards temples flaring elbows wide and rotating wrists up', 'Hold rear delt squeeze for 1 second'],
            commonMistakes: ['Pulling straight down to chest', 'Using too much weight causing body jerk'],
            alternative1: 'Bent-Over Dumbbell Rear Delt Flyes',
            alternative2: 'Reverse Pec Deck Machine'
          }
        ]
      },
      {
        dayIndex: 3,
        name: 'Leg Day (Quads, Hamstrings & Calves)',
        focus: 'Lower Body Strength & Power',
        exercises: [
          {
            id: 'g8',
            name: 'Barbell Back Squats',
            sets: 4,
            reps: '6-10',
            targetMuscle: 'Quadriceps, Glutes & Core',
            equipment: 'Barbell, Squat Rack',
            instructions: ['Rest bar firmly across upper traps', 'Hinge at hips and knees simultaneously, sinking below parallel', 'Drive up explosively through mid-foot with knees tracking toes'],
            commonMistakes: ['Knees collapsing inwards on ascent', 'Heels peeling off the floor'],
            alternative1: 'Goblet Squats with Heavy Dumbbell',
            alternative2: 'Leg Press Machine'
          },
          {
            id: 'g9',
            name: 'Romanian Deadlifts (RDL)',
            sets: 4,
            reps: '8-12',
            targetMuscle: 'Hamstrings & Gluteal Muscles',
            equipment: 'Barbell or Pair of Dumbbells',
            instructions: ['Maintain soft knee bend throughout entire lift', 'Push hips back towards the wall behind you while lowering bar down shins', 'Feel intense hamstring stretch then drive hips forward to lockout'],
            commonMistakes: ['Squatting the weight down instead of hip hinging', 'Rounding lumbar spine'],
            alternative1: 'Lying Leg Curls Machine',
            alternative2: 'Single-Leg Dumbbell RDL'
          },
          {
            id: 'g10',
            name: 'Walking Dumbbell Lunges',
            sets: 3,
            reps: '12 per leg',
            targetMuscle: 'Quads, Glutes & Balance',
            equipment: 'Dumbbells',
            instructions: ['Take long stride forward keeping torso upright', 'Lower back knee until it hovers 1 inch above floor', 'Push off front foot to step through into next rep'],
            commonMistakes: ['Stepping in a narrow tightrope line losing balance', 'Front knee shooting far past toes'],
            alternative1: 'Bulgarian Split Squats',
            alternative2: 'Step-ups on Bench'
          },
          {
            id: 'g20',
            name: 'Standing Calf Raises',
            sets: 4,
            reps: '15-20',
            targetMuscle: 'Gastrocnemius & Soleus',
            equipment: 'Calf Machine or Step Platform',
            instructions: ['Stand with balls of feet on edge', 'Drop heels into deep stretch at bottom for 1 second', 'Drive up onto toes as high as possible and squeeze'],
            commonMistakes: ['Bouncing fast reps using Achilles tendon elasticity', 'Bending knees'],
            alternative1: 'Seated Calf Raises',
            alternative2: 'Single-Leg Bodyweight Calf Raises'
          }
        ]
      },
      {
        dayIndex: 4,
        name: 'Core & Conditioning (Abs & Cardio)',
        focus: 'Core Stability & Metabolic Conditioning',
        exercises: [
          {
            id: 'g11',
            name: 'Hanging Leg Raises',
            sets: 3,
            reps: '12-15',
            targetMuscle: 'Lower Rectus Abdominis',
            equipment: 'Pull-Up Bar',
            instructions: ['Hang with straight arms and tight grip', 'Roll pelvis up while lifting feet to waist level', 'Avoid swinging torso by engaging lats'],
            commonMistakes: ['Swinging legs using hip flexor momentum', 'Arching lower back at bottom'],
            alternative1: 'Lying Captain Chair Leg Raises',
            alternative2: 'Reverse Crunches'
          },
          {
            id: 'g12',
            name: 'Forearm Plank with Squeeze',
            sets: 3,
            reps: '60 seconds',
            targetMuscle: 'Transverse Abdominis & Deep Core',
            equipment: 'Gym Mat / Floor',
            instructions: ['Rest on forearms directly below shoulders', 'Brace abdominal wall like anticipating a punch', 'Squeeze glutes and thighs into rigid straight line'],
            commonMistakes: ['Letting hips sag down pinching lumbar spine', 'Piking hips too high in air'],
            alternative1: 'Knee Plank Hold',
            alternative2: 'Ab Wheel Rollouts'
          },
          {
            id: 'g21',
            name: 'Weighted Russian Twists',
            sets: 3,
            reps: '20 reps',
            targetMuscle: 'Internal & External Obliques',
            equipment: 'Dumbbell or Medicine Ball',
            instructions: ['Sit on floor with knees bent, feet lifted 3 inches', 'Rotate torso from side to side tapping weight next to hips', 'Keep chest open and core braced'],
            commonMistakes: ['Rounding lower back slouching backwards', 'Moving only arms without torso rotation'],
            alternative1: 'Bicycle Crunches',
            alternative2: 'Side Plank Hip Dips'
          }
        ]
      }
    ]
  },
  {
    id: 'routine-calisthenics',
    name: 'Home Calisthenics & Bodyweight Mastery',
    description: 'Zero-equipment full body calisthenics routine designed for home workouts and functional conditioning.',
    focus: 'Functional Strength & Fat Burn',
    daysPerWeek: 3,
    isCustom: false,
    createdAt: '2026-01-01',
    splits: [
      {
        dayIndex: 1,
        name: 'Upper Body Bodyweight Power',
        focus: 'Chest, Shoulders & Triceps',
        exercises: [
          {
            id: 'h1',
            name: 'Standard Floor Pushups',
            sets: 4,
            reps: '15-20',
            targetMuscle: 'Chest, Triceps & Core',
            equipment: 'Floor',
            instructions: ['Hands slightly wider than shoulders', 'Body rigid like a moving plank', 'Chest touches floor, lock out elbows'],
            commonMistakes: ['Sagging lower back', 'Flaring elbows 90 degrees out'],
            alternative1: 'Incline Pushups (Hands on Table)',
            alternative2: 'Knee Pushups'
          },
          {
            id: 'h2',
            name: 'Decline Elevated Pushups',
            sets: 4,
            reps: '12-15',
            targetMuscle: 'Clavicular Head (Upper Chest)',
            equipment: 'Bed or Chair',
            instructions: ['Place feet on chair or bed edge', 'Lower chest smoothly between hands', 'Press up focusing on upper chest contraction'],
            commonMistakes: ['Dropping hips downward', 'Shrugging shoulders'],
            alternative1: 'Standard Pushups',
            alternative2: 'Pike Pushups'
          },
          {
            id: 'h3',
            name: 'Pike Pushups',
            sets: 3,
            reps: '10-12',
            targetMuscle: 'Shoulders & Triceps',
            equipment: 'Floor',
            instructions: ['Hips high in air forming an inverted V-shape', 'Lower top of head toward ground in front of hands', 'Press straight back up into V-shape'],
            commonMistakes: ['Flaring elbows out', 'Rounding spine'],
            alternative1: 'Elevated Pike Pushups',
            alternative2: 'Wall Handstand Hold'
          },
          {
            id: 'h4',
            name: 'Chair / Bench Dips',
            sets: 3,
            reps: '12-15',
            targetMuscle: 'Triceps & Lower Chest',
            equipment: 'Sturdy Chair',
            instructions: ['Grip chair edge with hands right next to hips', 'Lower body until elbows reach 90 degrees', 'Push through palms to full lockout'],
            commonMistakes: ['Letting hips drift away from chair', 'Going excessively deep straining shoulder capsule'],
            alternative1: 'Diamond Pushups',
            alternative2: 'Floor Triceps Extensions'
          }
        ]
      },
      {
        dayIndex: 2,
        name: 'Lower Body & Explosive Cardio',
        focus: 'Legs, Glutes & Calves',
        exercises: [
          {
            id: 'h5',
            name: 'Bodyweight Air Squats',
            sets: 4,
            reps: '20-25',
            targetMuscle: 'Quads & Glutes',
            equipment: 'Floor',
            instructions: ['Feet shoulder-width apart, toes turned 15 degrees out', 'Squat down below parallel keeping chest high', 'Drive through mid-foot to stand'],
            commonMistakes: ['Knees caving inwards', 'Heels coming off ground'],
            alternative1: 'Chair Squats',
            alternative2: 'Jump Squats'
          },
          {
            id: 'h6',
            name: 'Reverse Lunges',
            sets: 3,
            reps: '15 per leg',
            targetMuscle: 'Quads, Hamstrings & Glutes',
            equipment: 'Floor',
            instructions: ['Step backward with right leg', 'Lower until both knees are at 90 degrees', 'Drive through front heel to return'],
            commonMistakes: ['Slamming rear knee on ground', 'Leaning torso excessively forward'],
            alternative1: 'Forward Lunges',
            alternative2: 'Bulgarian Split Squats'
          },
          {
            id: 'h7',
            name: 'Single-Leg Glute Bridges',
            sets: 3,
            reps: '12 per leg',
            targetMuscle: 'Glutes & Hamstrings',
            equipment: 'Floor / Mat',
            instructions: ['Lie on back, bend knees with feet flat', 'Extend one leg straight, push hips up with other leg', 'Squeeze glute at top for 2 seconds'],
            commonMistakes: ['Hyperextending lower back', 'Not achieving full hip extension'],
            alternative1: 'Double-Leg Glute Bridges',
            alternative2: 'Hip Thrust on Couch'
          }
        ]
      },
      {
        dayIndex: 3,
        name: 'Core Shred & HIIT Circuit',
        focus: 'Six-Pack Abs & Endurance',
        exercises: [
          {
            id: 'h8',
            name: 'Bicycle Crunches',
            sets: 3,
            reps: '25 per side',
            targetMuscle: 'Rectus Abdominis & Obliques',
            equipment: 'Floor / Mat',
            instructions: ['Lie on back, hands behind ears', 'Bring opposite elbow to opposite knee while extending other leg', 'Rotate shoulders smoothly with continuous breathing'],
            commonMistakes: ['Yanking neck with hands', 'Going too fast with shallow rotation'],
            alternative1: 'Standard Crunches',
            alternative2: 'Deadbug'
          },
          {
            id: 'h9',
            name: 'Mountain Climbers',
            sets: 3,
            reps: '40 reps total',
            targetMuscle: 'Core & Cardiovascular System',
            equipment: 'Floor',
            instructions: ['Start in pushup plank position', 'Drive knees alternately toward chest at rapid pace', 'Keep hips level without bouncing up into air'],
            commonMistakes: ['Bouncing hips in air', 'Hands placed too far forward'],
            alternative1: 'High Knees',
            alternative2: 'Plank Knee Taps'
          }
        ]
      }
    ]
  }
]

/**
 * Yoga & Zumba Cardio sessions matching Android YogaZumbaScreen.kt
 */
export const CARDIO_SESSIONS: CardioSession[] = [
  {
    id: 'y1',
    name: 'Morning Flexibility Flow',
    durationMinutes: 15,
    caloriesBurned: 80,
    difficulty: 'Easy',
    category: 'Yoga',
    benefits: 'Improves hamstring range, relieves spinal compression, and energizes joints',
    steps: ["Child's Pose (2 mins)", "Cat-Cow Dynamic Spine Stretch (3 mins)", "Downward Facing Dog (3 mins)", "Cobra Lumbar Stretch (3 mins)", "Warrior I & II Pose (4 mins)"]
  },
  {
    id: 'y2',
    name: 'Back Pain Relief & Posture Reset',
    durationMinutes: 10,
    caloriesBurned: 60,
    difficulty: 'Easy',
    category: 'Yoga',
    benefits: 'Decompresses lumbar disc pressure and realigns slouched posture from desk work',
    steps: ["Knee-to-Chest Stretch (2 mins)", "Reclined Spinal Twist (3 mins)", "Bridge Hip Raise Pose (2 mins)", "Gentle Cat-Cow Flow (3 mins)"]
  },
  {
    id: 'y3',
    name: 'Rest-Day Deep Muscle Recovery',
    durationMinutes: 20,
    caloriesBurned: 110,
    difficulty: 'Medium',
    category: 'Yoga',
    benefits: 'Accelerates lactic acid drainage and restores range of motion in tight muscle groups',
    steps: ["Pigeon Pose (4 mins per leg)", "Butterfly Hip Opener (3 mins)", "Hamstring Strap Stretch (4 mins)", "Corpse Pose Deep Relaxation (4 mins)"]
  },
  {
    id: 'z1',
    name: 'Beginner Zumba Beat Dance',
    durationMinutes: 15,
    caloriesBurned: 160,
    difficulty: 'Easy',
    category: 'Zumba',
    benefits: 'Fun, fat-burning Latin rhythms with simple footwork anyone can groove to',
    steps: ["Basic Merengue March (3 mins)", "Reggaeton Side-Step Bounce (4 mins)", "Salsa Forward Hip Step (4 mins)", "Cooldown Breathing Waves (4 mins)"]
  },
  {
    id: 'z2',
    name: 'Latin Salsa Cardio Blast',
    durationMinutes: 20,
    caloriesBurned: 220,
    difficulty: 'Medium',
    category: 'Zumba',
    benefits: 'High-energy salsa footwork targeting abdominal toning, stamina, and calorie burn',
    steps: ["Salsa Quick Shuffle (5 mins)", "Cumbia Hip Slide (5 mins)", "Cha-Cha-Cha Step (5 mins)", "Rhythm Cooldown (5 mins)"]
  },
  {
    id: 'z3',
    name: '10-Minute Rapid Beat Burn',
    durationMinutes: 10,
    caloriesBurned: 120,
    difficulty: 'Hard',
    category: 'Zumba',
    benefits: 'Fast-tempo HIIT dance intervals to blast stubborn calories in minimal time',
    steps: ["High Knee Beat Bounce (3 mins)", "Zumba Hip Roll Drops (3 mins)", "Fast Salsa Quick Shuffle (2 mins)", "Heart Rate Cooldown (2 mins)"]
  }
]

/**
 * Budget-friendly wholesome food recommendations from EchoAIEngine.kt
 */
export const HEALTHY_FOOD_PRESETS: Omit<MealItem, 'id'>[] = [
  { name: 'Roasted Chana (Bengal Gram)', portion: '50g', calories: 180, protein: 11, carbs: 29, fats: 3, category: 'Snacks' },
  { name: 'Sprouts & Moong Salad with Lemon', portion: '1 Bowl (150g)', calories: 140, protein: 12, carbs: 22, fats: 1, category: 'Snacks' },
  { name: 'Boiled Eggs (3 Whole)', portion: '3 Eggs', calories: 210, protein: 18, carbs: 2, fats: 14, category: 'Breakfast' },
  { name: 'Paneer Tikka / Grilled Paneer', portion: '150g', calories: 360, protein: 26, carbs: 6, fats: 25, category: 'Lunch' },
  { name: 'Dal Tadka with Steamed Rice', portion: '1 Bowl Dal + 1 Cup Rice', calories: 380, protein: 15, carbs: 68, fats: 6, category: 'Lunch' },
  { name: 'Grilled Chicken Breast with Salad', portion: '200g Chicken', calories: 330, protein: 48, carbs: 4, fats: 8, category: 'Dinner' },
  { name: 'Greek Yogurt with Banana & Walnuts', portion: '1 Bowl', calories: 240, protein: 16, carbs: 30, fats: 7, category: 'Breakfast' },
  { name: 'Soya Chunks Bhurji with 2 Rotis', portion: '1 Bowl + 2 Rotis', calories: 420, protein: 32, carbs: 54, fats: 8, category: 'Dinner' },
  { name: 'Peanut Butter on Whole Wheat Toast', portion: '2 Slices', calories: 290, protein: 12, carbs: 32, fats: 14, category: 'Breakfast' },
  { name: 'Tofu & Vegetable Stir-Fry', portion: '1 Plate (200g)', calories: 220, protein: 18, carbs: 12, fats: 10, category: 'Dinner' }
]

/**
 * Guided Wellness Programs matching Image 1 (Morning Reset, Deep Focus, etc.)
 */
export const DEFAULT_GUIDED_PROGRAMS: GuidedProgram[] = [
  {
    id: 'prog_morning_reset',
    title: 'Morning Reset',
    subtitle: 'Mobility & Diaphragmatic Breath',
    completedSessions: 2,
    totalSessions: 5,
    durationMinutes: 10,
    benefits: 'Activates sympathetic alertness, decompresses cervical spine, and flushes cortisol.',
    steps: [
      'Box Breathing 4-4-4-4 (2 mins)',
      'Spinal Cat-Cow Waves (3 mins)',
      'World Greatest Stretch (3 mins)',
      'Upright Thoracic Opener (2 mins)'
    ],
    auraColor: '#84CC16'
  },
  {
    id: 'prog_deep_focus',
    title: 'Deep Focus',
    subtitle: 'Pre-Workout Neural Priming',
    completedSessions: 0,
    totalSessions: 5,
    durationMinutes: 12,
    benefits: 'Primes motor unit recruitment, stabilizes rotator cuffs, and elevates heart rate variability.',
    steps: [
      'Isometric Glute Bridges (3 mins)',
      'Band Pull-Aparts & Dislocates (3 mins)',
      'Scapular Wall Slides (3 mins)',
      'Metabolic Jump Rope or High Knees (3 mins)'
    ],
    auraColor: '#A3E635'
  },
  {
    id: 'prog_active_recovery',
    title: 'Active Recovery Flow',
    subtitle: 'Myofascial Release & Lymph Flush',
    completedSessions: 3,
    totalSessions: 5,
    durationMinutes: 15,
    benefits: 'Reduces delayed onset muscle soreness (DOMS) and accelerates nutrient delivery.',
    steps: [
      'Quad & Hamstring Foam Rolling (5 mins)',
      'Reclined Pigeon Pose (4 mins)',
      'Supine Spinal Twist (3 mins)',
      'Elevated Legs-on-Wall Rest (3 mins)'
    ],
    auraColor: '#65A30D'
  },
  {
    id: 'prog_night_wind_down',
    title: 'Night Sleep Prep',
    subtitle: 'Vagus Nerve Activation',
    completedSessions: 4,
    totalSessions: 5,
    durationMinutes: 8,
    benefits: 'Shifts nervous system from sympathetic fight-or-flight into restorative REM sleep.',
    steps: [
      '4-7-8 Parasympathetic Breathing (3 mins)',
      'Child\'s Pose with Extended Reach (3 mins)',
      'Gentle Neck Lateral Release (2 mins)'
    ],
    auraColor: '#4D7C0F'
  }
]

/**
 * Initial Daily Recovery Metrics matching Image 1 ("Recovery today 86%")
 */
export const INITIAL_RECOVERY_METRICS: RecoveryMetrics = {
  score: 86,
  status: 'Optimal Readiness',
  sleepHours: 7.8,
  hrvMs: 68,
  sorenessLevel: 'Low',
  trend: [68, 74, 78, 82, 86]
}

/**
 * Calendar Strip with colored compliance dots matching Image 1
 */
export const INITIAL_CALENDAR_DAYS: CalendarDayItem[] = [
  { date: 12, dayName: 'mon', dots: ['#C084FC'], isToday: false },
  { date: 13, dayName: 'mon', dots: ['#F472B6', '#22D3EE'], isToday: false },
  { date: 14, dayName: 'mon', dots: ['#34D399'], isToday: false },
  { date: 15, dayName: 'mon', dots: ['#FBBF24', '#A3E635'], isToday: true }
]
