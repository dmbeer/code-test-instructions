interface ConfirmDeleteModalProps {
    item: URLSResponse;
    onCancel: () => void;
    onConfirm: () => void;
}

const ConfirmDeleteModal: React.FC<ConfirmDeleteModalProps> = ({
                                                                   item,
                                                                   onCancel,
                                                                   onConfirm,
                                                               }) => (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
        <div
            className="absolute inset-0 bg-black/50"
            onClick={onCancel}
        />

        <div className="relative z-20 max-w-md rounded bg-white p-6">
            <h2 className="text-lg font-semibold text-red-400">
                Delete this item?
            </h2>

            <p className="mt-2 text-sm text-gray-600 truncate">
                {item.shortUrl}
            </p>

            <div className="mt-6 flex justify-end gap-3">
                <button className="text-black" onClick={onCancel}>Cancel</button>
                <button
                    onClick={onConfirm}
                    className="bg-red-600 px-4 py-2 text-white"
                >
                    Delete
                </button>
            </div>
        </div>
    </div>
);

export default ConfirmDeleteModal;