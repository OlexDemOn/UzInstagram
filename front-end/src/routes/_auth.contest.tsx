import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_auth/contest')({
  component: () => <div>Hello /__authenticated!</div>,
})
