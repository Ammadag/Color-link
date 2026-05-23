# Color Link Design System

## Design direction

Color Link should feel like a premium, polished puzzle game rather than a basic utility app.

The visual language is:

- Dark-first
- Soft glass panels
- Neon glowing dots and paths
- Rounded, friendly geometry
- Smooth micro-interactions
- Minimal but expressive motion
- Clear gameplay focus

The UI should preserve the premium feel of the refined gameplay, level selection, and settings screens. Avoid flat, plain, inconsistent, or overly bright visual treatment.

## Theme mode

Default theme: **dark**.

Light mode may be added later, but all current UI must be optimized for dark backgrounds.

## Color tokens

Use semantic tokens, not random hard-coded values.

| Token | Hex | Usage |
|---|---:|---|
| `background` | `#0B1326` | App background |
| `surface` | `#0B1326` | Main surface |
| `surfaceContainerLowest` | `#060E20` | Deep background |
| `surfaceContainerLow` | `#131B2E` | Board/cards base |
| `surfaceContainer` | `#171F33` | Cards, panels |
| `surfaceContainerHigh` | `#222A3D` | Elevated cards |
| `surfaceContainerHighest` | `#2D3449` | High emphasis surface |
| `surfaceBright` | `#31394D` | Bright surface accent |
| `surfaceVariant` | `#2D3449` | Secondary surfaces |
| `onBackground` | `#DAE2FD` | Text on background |
| `onSurface` | `#DAE2FD` | Primary text |
| `onSurfaceVariant` | `#C2C6D6` | Secondary text |
| `outline` | `#8C909F` | Borders, dividers |
| `outlineVariant` | `#424754` | Subtle borders |
| `primary` | `#ADC6FF` | Main blue accent |
| `primaryContainer` | `#4D8EFF` | Primary button/container |
| `onPrimary` | `#002E6A` | Text on primary |
| `onPrimaryContainer` | `#00285D` | Text/icon on primary container |
| `primaryFixed` | `#D8E2FF` | Fixed primary highlight |
| `primaryFixedDim` | `#ADC6FF` | Dim primary highlight |
| `secondary` | `#DDB7FF` | Purple accent |
| `secondaryContainer` | `#6F00BE` | Secondary container |
| `onSecondary` | `#490080` | Text on secondary |
| `onSecondaryContainer` | `#D6A9FF` | Text on secondary container |
| `tertiary` | `#4CD7F6` | Cyan accent |
| `tertiaryContainer` | `#009EB9` | Tertiary container |
| `onTertiary` | `#003640` | Text on tertiary |
| `onTertiaryContainer` | `#002F38` | Text on tertiary container |
| `error` | `#FFB4AB` | Error/invalid move |
| `errorContainer` | `#93000A` | Error container |
| `onError` | `#690005` | Text on error |
| `onErrorContainer` | `#FFDAD6` | Text on error container |

## Gameplay color tokens

Dot/path colors must be vivid and glow softly.

| Gameplay color | Hex | Glow |
|---|---:|---|
| Red | `#FFB4AB` | `rgba(255, 180, 171, 0.60)` |
| Blue | `#ADC6FF` | `rgba(173, 198, 255, 0.60)` |
| Cyan | `#4CD7F6` | `rgba(76, 215, 246, 0.60)` |
| Purple | `#DDB7FF` | `rgba(221, 183, 255, 0.60)` |
| Green | `#7EE7B8` | `rgba(126, 231, 184, 0.55)` |
| Yellow | `#FFE082` | `rgba(255, 224, 130, 0.55)` |

The first four colors match the refined design. Green and yellow are optional extension colors for higher levels. Do not use dull material defaults for dots.

## Gradients

Use gradients sparingly.

### App background

```text
Base: #0B1326
Radial glow: rgba(77, 142, 255, 0.10), top center, fade to transparent
```

### Primary button

```text
Top: primaryContainer #4D8EFF
Bottom: primaryFixedDim #ADC6FF
Shadow: rgba(77, 142, 255, 0.35)
```

### Board container

```text
Base: surfaceContainerLow #131B2E
Overlay: rgba(255, 255, 255, 0.05)
Border: rgba(255, 255, 255, 0.10)
Inset glow: rgba(255, 255, 255, 0.04)
```

## Typography

Use **Quicksand** for the whole app.

Place font files in:

```text
core/designsystem/src/main/res/font/
```

Recommended font files:

```text
quicksand_regular.ttf
quicksand_medium.ttf
quicksand_semibold.ttf
quicksand_bold.ttf
```

| Token | Size | Line height | Weight | Usage |
|---|---:|---:|---:|---|
| `displayLg` | 40sp | 48sp | 700 | Big play text, splash title |
| `headlineLg` | 32sp | 40sp | 700 | Large section title |
| `headlineMd` | 24sp | 32sp | 600 | Screen title, cards |
| `bodyLg` | 18sp | 26sp | 500 | Prominent body copy |
| `bodyMd` | 16sp | 24sp | 400 | Default text |
| `labelLg` | 14sp | 20sp | 600 | Buttons, chips |
| `labelSm` | 12sp | 16sp | 500 | Metadata, captions |

Letter spacing:

- `labelLg`: `0.05em`
- `displayLg`: `-0.02em`

## Spacing tokens

Base unit: **4dp**.

| Token | Value | Usage |
|---|---:|---|
| `unit` | 4dp | Fine spacing |
| `stackSm` | 8dp | Tight component spacing |
| `stackMd` | 16dp | Default vertical spacing |
| `gutter` | 16dp | Screen horizontal edge spacing |
| `containerPadding` | 24dp | Main content padding |
| `stackLg` | 32dp | Section spacing |

Screen rules:

- Use `containerPadding` for main content.
- Use `gutter` for app bars and compact components.
- Do not use arbitrary spacing values unless a component requires optical correction.

## Shape tokens

| Token | Value | Usage |
|---|---:|---|
| `default` | 16dp | Small cards/buttons |
| `large` | 32dp | Board, premium cards |
| `extraLarge` | 48dp | Hero containers |
| `full` | 9999dp | Pills, circles |

Gameplay board should use 24dp to 32dp radius depending on screen size.

## Elevation and glass treatment

### Glass panel

Use for cards, settings rows, secondary actions, counters, and nav surfaces.

```kotlin
Modifier
    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
    .border(1.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
```

Recommended shadow:

```text
0dp 8dp 32dp rgba(0, 0, 0, 0.40)
```

### Premium glass board

Use stronger depth for the gameplay board.

```text
Outer shadow: rgba(0, 0, 0, 0.60)
Inner glow: rgba(255, 255, 255, 0.06)
Border: rgba(255, 255, 255, 0.10)
```

## Core components

### `ColorLinkScaffold`

Use for regular screens.

Responsibilities:

- Applies background gradient.
- Handles safe area padding.
- Controls optional top app bar.
- Controls optional bottom navigation.

Gameplay screens may hide bottom navigation.

### `ColorLinkTopAppBar`

Use consistent top bars:

- Height: 64dp
- Background: `surface` at 80–90% opacity
- Blur-like feel using translucent background
- Bottom border: white 10% opacity
- Left: back/menu action
- Center: title/subtitle
- Right: counters or action icon

### `ColorLinkBottomNavBar`

Use only on high-level screens:

- Home
- Levels
- Daily puzzle
- Settings

Do not show it on gameplay unless the product decision changes.

### `GlassCard`

Use for level cards, settings rows, bento stats, and secondary action cards.

Rules:

- Rounded corners: 16dp or 32dp
- Background alpha: 5–8% white
- Border alpha: 8–12% white
- Press scale: 0.95

### `PrimaryGlowButton`

Use for the main `PLAY` action and high-priority CTAs.

Rules:

- Height: 64–80dp
- Shape: full pill
- Gradient fill
- Blue glow shadow
- Press scale: 0.95
- Optional slow glow pulse

### `ControlCircleButton`

Use for gameplay controls:

- Undo
- Hint
- Restart

Rules:

- Circular or near-circular shape
- Hint button is visually primary
- Undo and restart are secondary glass buttons
- Icon above label
- Hint count badge uses `secondaryContainer`

### `GameBoard`

Board requirements:

- Square aspect ratio
- Max width around 500dp on phones/tablets
- Rounded premium glass container
- Inner grid uses equal cells
- Grid gap: 4dp to 8dp depending on board size
- Cells have very subtle glass fill and border
- Board should be visually centered but not hidden behind controls

### `DotNode`

Dot requirements:

- Circular
- Size: 55–65% of cell size
- Glow shadow matching dot color
- Active dot scale: 1.05–1.12
- Completed dot may have stable glow

### `PathSegment`

Path requirements:

- Rounded ends
- Width: 25–32% of cell size
- Glow matches color
- Corners must feel continuous, not square or broken
- Use Canvas for best results where possible

## Animation system

Keep animation smooth and premium, not cartoonish.

| Animation | Duration | Easing | Usage |
|---|---:|---|---|
| Press scale | 100–150ms | FastOutSlowIn | Buttons/cards |
| Screen enter | 250–350ms | FastOutSlowIn | Screen content fade/slide |
| Dot pulse | 1800–2200ms | EaseInOut | Available endpoints |
| Button glow pulse | 2600–3200ms | EaseInOut | Primary play/hint button |
| Path draw | 80–140ms per segment | LinearOutSlowIn | Drag path feedback |
| Invalid shake | 180–240ms | FastOutLinearIn | Invalid drag move |
| Win celebration | 700–1200ms | FastOutSlowIn | Level completion |

### Motion rules

- Use motion to confirm intent, not distract.
- Do not animate every grid cell continuously.
- Dots may pulse subtly; completed paths should be calm.
- Respect system reduced-animation settings where possible.

## Haptics and sound

Use haptics sparingly:

- Light tick when a valid cell is added.
- Stronger tick when a path is completed.
- Error vibration on invalid move.
- Celebration haptic when level is completed.

Sound should be optional and controlled from settings.

## Accessibility

- Maintain contrast between text and dark backgrounds.
- All tappable items should be at least 48dp.
- Provide content descriptions for controls.
- Do not rely on color alone for locked/completed level states; use icons too.
- Support font scaling without clipping major screens.

## Compose implementation tokens

Create tokens inside `:core:designsystem`.

Recommended files:

```text
ColorLinkColors.kt
ColorLinkTypography.kt
ColorLinkSpacing.kt
ColorLinkShapes.kt
ColorLinkTheme.kt
ColorLinkMotion.kt
```

Do not duplicate token values inside feature modules.
