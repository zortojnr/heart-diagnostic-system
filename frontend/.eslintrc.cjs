module.exports = {
  root: true,
  env: {
    node: true,
    browser: true,
    es2022: true,
  },
  extends: [
    'eslint:recommended',
  ],
  parserOptions: {
    ecmaVersion: 'latest',
    sourceType: 'module',
  },
  ignorePatterns: [
    'dist',
    '*.vue',
    '*.d.ts',
    '*.ts',
    'tailwind.config.js',
    'vite.config.js',
    '.eslintrc.cjs'
  ],
  rules: {
    'no-unused-vars': 'off',
    'no-console': 'warn',
    'no-redeclare': 'off',
  },
}
