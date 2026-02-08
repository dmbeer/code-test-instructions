interface RowActionsProps {
    item: URLSResponse,
    onRequestDelete: (item: URLSResponse) => void;
}

const RowActions: React.FC<RowActionsProps> = ({ item, onRequestDelete }) => {

    const handleDelete = () => {
        // confirm + API call
        console.log("Delete", item.alias);
    };

    return (
        <div className="flex gap-2">
            <button
                className="text-red-600 hover:underline"
                onClick={() => onRequestDelete(item)}
            >
                Delete
            </button>
        </div>
    );
};

export default RowActions;
