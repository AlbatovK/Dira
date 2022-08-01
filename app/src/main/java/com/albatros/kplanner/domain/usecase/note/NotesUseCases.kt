package com.albatros.kplanner.domain.usecase.note

data class NotesUseCases(
    val addNotesToScheduleById: AddNotesToScheduleByIdUseCase,
    val addNotesToSchedule: AddNotesToScheduleUseCase,
    val changeNotePosition: ChangeNotePositionUseCase,
    val finishNote: FinishNoteUseCase,
    val loadAllNotes: LoadAllNotesUseCase,
    val removeNoteAtFromSchedule: RemoveNoteAtFromScheduleUseCase
)