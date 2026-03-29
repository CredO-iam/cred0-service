export const surfaceThemeClasses = {
  dark: 'border-slate-800 bg-slate-900',
  light: 'border-slate-200 bg-white',
}

export const getSurfaceThemeClass = (isDarkTheme) =>
  isDarkTheme ? surfaceThemeClasses.dark : surfaceThemeClasses.light
